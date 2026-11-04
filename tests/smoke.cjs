const { chromium } = require('playwright');
const assert = require('node:assert/strict');

(async () => {
  const base = process.env.SMOKE_BASE_URL || 'http://127.0.0.1:8080';
  const suffix = Date.now().toString(36);
  const username = 'smoke_' + suffix;
  const password = 'strong-smoke-password-' + suffix;

  async function api(path, options = {}) {
    const response = await fetch(base + path, {
      ...options,
      headers: { 'Content-Type': 'application/json', ...(options.headers || {}) },
    });
    return { status: response.status, body: await response.json().catch(() => null) };
  }

  const health = await fetch(base + '/actuator/health');
  assert.equal(health.status, 200, 'Health endpoint should be available');
  assert.equal((await health.json()).status, 'UP');

  const registration = await api('/auth/register', {
    method: 'POST',
    body: JSON.stringify({ username, password }),
  });
  assert.equal(registration.status, 200, JSON.stringify(registration.body));

  const login = await api('/auth/login', {
    method: 'POST',
    body: JSON.stringify({ username, password }),
  });
  assert.equal(login.status, 200);
  assert.ok(login.body?.token, 'Login must return a JWT');

  const headers = { Authorization: 'Bearer ' + login.body.token };
  const contact = {
    name: 'Contato Smoke',
    phone: '11999999999',
    email: 'smoke@example.com',
    type: 'WORK',
    address: { street: 'Rua Exemplo', city: 'São Paulo', state: 'SP', zipCode: '01000-000' },
  };
  const created = await api('/contacts', { method: 'POST', headers, body: JSON.stringify(contact) });
  assert.equal(created.status, 201, JSON.stringify(created.body));
  assert.ok(created.body?.id);
  const id = created.body.id;

  const list = await api('/contacts', { headers });
  assert.equal(list.status, 200);
  assert.ok(list.body.some(c => c.id === id));
  const changed = await api('/contacts/' + id, {
    method: 'PUT', headers, body: JSON.stringify({ ...contact, name: 'Contato Atualizado' }),
  });
  assert.equal(changed.status, 200);
  assert.equal(changed.body.name, 'Contato Atualizado');

  const denied = await api('/contacts/' + id, { method: 'DELETE', headers });
  assert.equal(denied.status, 403, 'USER should not be allowed to delete');

  const second = 'smoke_other_' + suffix;
  assert.equal((await api('/auth/register', {
    method: 'POST', body: JSON.stringify({ username: second, password }),
  })).status, 200);
  const secondLogin = await api('/auth/login', {
    method: 'POST', body: JSON.stringify({ username: second, password }),
  });
  assert.equal(secondLogin.status, 200);
  const secondHeaders = { Authorization: 'Bearer ' + secondLogin.body.token };
  const otherList = await api('/contacts', { headers: secondHeaders });
  assert.equal(otherList.status, 200);
  assert.equal(otherList.body.length, 0, 'A second user must not see first user contacts');
  assert.equal((await api('/contacts/' + id, { headers: secondHeaders })).status, 404);

  const browser = await chromium.launch({ headless: true });
  const page = await browser.newPage({ viewport: { width: 1280, height: 900 } });
  try {
    await page.goto(base, { waitUntil: 'networkidle' });
    await page.locator('#username').fill(username);
    await page.locator('#password').fill(password);
    await page.locator('#auth-submit').click();
    await page.locator('#app-screen:not(.hidden)').waitFor({ timeout: 20000 });
    await page.getByText('Contato Atualizado', { exact: true }).waitFor();
    await page.locator('#new-contact').click();
    await page.locator('#contact-name').fill('Contato pelo Navegador');
    await page.locator('#contact-type').selectOption('FRIEND');
    await page.locator('#save-contact').click();
    await page.getByText('Contato pelo Navegador', { exact: true }).waitFor();
    await page.locator('#search').fill('Contato pelo Navegador');
    await page.getByText('Contato Atualizado', { exact: true }).waitFor({ state: 'hidden' });
    await page.setViewportSize({ width: 390, height: 844 });
    assert.equal(await page.locator('#new-contact').isVisible(), true);
    console.log('PASS: PostgreSQL API CRUD, JWT authorization, isolation and Chromium UI flow');
  } finally {
    await browser.close();
  }
})().catch(error => { console.error(error); process.exit(1); });
