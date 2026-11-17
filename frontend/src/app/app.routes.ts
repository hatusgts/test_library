import { Routes } from '@angular/router';
import { authGuard } from './core/auth.guard';
import { CrudConfig } from './core/models';
import { CadastroPage } from './pages/cadastro-page';
import { CrudPage } from './pages/crud-page';
import { LoginPage } from './pages/login-page';

const livros: CrudConfig = {
  titulo: 'Livros',
  recurso: 'livros',
  campos: [
    { chave: 'titulo', rotulo: 'Título', tipo: 'text' },
    { chave: 'autor', rotulo: 'Autor', tipo: 'text' },
    { chave: 'preco', rotulo: 'Preço', tipo: 'number' },
    { chave: 'categoria', rotulo: 'Categoria', tipo: 'text' },
    { chave: 'estoque', rotulo: 'Estoque', tipo: 'number' },
  ],
};

const clientes: CrudConfig = {
  titulo: 'Clientes',
  recurso: 'clientes',
  campos: [
    { chave: 'nome', rotulo: 'Nome', tipo: 'text' },
    { chave: 'email', rotulo: 'E-mail', tipo: 'email' },
    { chave: 'cpf', rotulo: 'CPF', tipo: 'text' },
    { chave: 'telefone', rotulo: 'Telefone', tipo: 'text' },
    { chave: 'cidade', rotulo: 'Cidade', tipo: 'text' },
  ],
};

const usuarios: CrudConfig = {
  titulo: 'Usuários',
  recurso: 'usuarios',
  campos: [
    { chave: 'nome', rotulo: 'Nome', tipo: 'text' },
    { chave: 'email', rotulo: 'E-mail', tipo: 'email' },
    { chave: 'senha', rotulo: 'Senha', tipo: 'password' },
  ],
};

export const routes: Routes = [
  { path: 'login', component: LoginPage },
  { path: 'cadastro', component: CadastroPage },
  { path: 'livros', component: CrudPage, canActivate: [authGuard], data: { config: livros } },
  { path: 'clientes', component: CrudPage, canActivate: [authGuard], data: { config: clientes } },
  { path: 'usuarios', component: CrudPage, canActivate: [authGuard], data: { config: usuarios } },
  { path: '', pathMatch: 'full', redirectTo: 'livros' },
  { path: '**', redirectTo: 'livros' },
];
