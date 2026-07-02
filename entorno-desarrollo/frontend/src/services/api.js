const API_BASE = '/api/tasks';

const getToken = () => localStorage.getItem('token');

const authHeaders = () => ({
  'Content-Type': 'application/json',
  'Authorization': `Bearer ${getToken()}`
});

export const getTasks = async () => {
  const res = await fetch(API_BASE, { headers: authHeaders() });
  if (!res.ok) throw new Error('Error al obtener tareas');
  return res.json();
};

export const createTask = async (task) => {
  const res = await fetch(API_BASE, {
    method: 'POST',
    headers: authHeaders(),
    body: JSON.stringify(task),
  });
  if (!res.ok) throw new Error('Error al crear tarea');
  return res.json();
};

export const updateTask = async (id, task) => {
  const res = await fetch(`${API_BASE}/${id}`, {
    method: 'PUT',
    headers: authHeaders(),
    body: JSON.stringify(task),
  });
  if (!res.ok) throw new Error('Error al actualizar tarea');
  return res.json();
};

export const deleteTask = async (id) => {
  const res = await fetch(`${API_BASE}/${id}`, {
    method: 'DELETE',
    headers: authHeaders(),
  });
  if (!res.ok) throw new Error('Error al eliminar tarea');
  return res.json();
};
