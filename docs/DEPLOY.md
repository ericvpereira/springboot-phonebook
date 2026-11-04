# Deploy do Phonebook

## Rodar com Docker Compose
1. Copie `.env.example` para `.env`.
2. Substitua as senhas de exemplo e configure `JWT_SECRET` com uma chave aleatória forte (mínimo de 32 bytes). Nunca versione `.env`.
3. Execute `docker compose up --build -d`.
4. Acesse `http://localhost:8080/`. Endpoint de saúde: `/actuator/health`.
5. Execute os testes com `./mvnw clean verify` e `node --test tests/frontend.test.cjs`.

## Produção (plataforma com suporte a Docker)
- Build com `Dockerfile`; expor porta 8080. O processo aceita `PORT` configurada pelo provedor.
- Variáveis obrigatórias: `SPRING_DATASOURCE_URL` (`jdbc:postgresql://host:5432/database`), `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, `JWT_SECRET`.
- O PostgreSQL deve ser persistente, com backup; habilite HTTPS via domínio/proxy da plataforma.
- Configure healthcheck HTTP em `/actuator/health`.
- Teste login, cadastro, CRUD, permissões de USER/ADMIN e celular antes de divulgar.

## Avisos de segurança e migração
**Antes de expor um banco existente:** versões anteriores criavam um usuário `admin` com senha fixa. O código não cria mais essa conta, mas **uma conta antiga continua no banco** até que você a desative ou altere a senha; rotacione a senha antes de publicar.

O provisionamento opcional de admin exige ambas `PHONEBOOK_ADMIN_USERNAME` e `PHONEBOOK_ADMIN_PASSWORD` (12 ou mais caracteres), e não altera contas existentes.

A migração `V9__contact_ownership.sql` isola contatos por usuário. Registros legados ficam sem proprietário e invisíveis por padrão, sem serem apagados. Faça backup e atribua manualmente o proprietário correto antes de recuperar os contatos. Exemplo apenas quando confirmado que os contatos legados pertencem ao usuário indicado:

```sql
-- UPDATE tb_contacts SET owner_username = 'usuario_correto' WHERE owner_username IS NULL;
```

Favoritos ficam somente no navegador; o JWT fica em memória e um reload exige login novamente. Não há recuperação de senha e email verificado. CI testa Java, contrato estático do frontend e build Docker; navegador real e banco em produção exigem validação separada.
