export interface Campo {
  chave: string;
  rotulo: string;
  tipo: 'text' | 'number' | 'email' | 'password';
}

export interface CrudConfig {
  titulo: string;
  recurso: string;
  campos: Campo[];
}

export type Registro = { id: number } & Record<string, string | number>;
