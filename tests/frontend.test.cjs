const test = require('node:test');
const assert = require('node:assert/strict');
const fs = require('node:fs');

const html = fs.readFileSync('src/main/resources/static/index.html', 'utf8');
const js = fs.readFileSync('src/main/resources/static/assets/phonebook.js', 'utf8');
const css = fs.readFileSync('src/main/resources/static/assets/phonebook.css', 'utf8');

test('page exposes authentication, contacts and CRUD controls', () => {
  for (const id of ['auth-form','username','password','contact-list','contact-form','new-contact','contact-dialog','logout','search']) {
    assert.match(html, new RegExp('id="' + id + '"'));
  }
});
test('frontend uses relative same-origin API routes and Bearer authorization', () => {
  for (const api of ['/auth/login','/auth/register','/contacts']) assert.ok(js.includes(api));
  assert.match(js, /headers\.Authorization\s*=\s*"Bearer "/);
  assert.doesNotMatch(js, /localhost:8080|localStorage\.setItem\("token"/);
});
test('contact data is escaped before writing into list markup', () => {
  assert.match(js, /function render\(/);
  assert.match(js, /escapeHtml\(c\.name\)/);
  assert.match(js, /escapeHtml\(c\.email/);
});
test('UI is responsive', () => {
  assert.match(css, /@media\(max-width:760px\)/);
});
