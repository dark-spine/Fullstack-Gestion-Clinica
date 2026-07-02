import { useState, useEffect } from 'react';
import { getTasks, deleteTask, updateTask } from '../services/api';

export default function TaskList({ refresh, onEdit }) {
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(true);

  const loadTasks = async () => {
    try {
      const data = await getTasks();
      setTasks(data);
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadTasks();
  }, [refresh]);

  const handleToggle = async (id, completed) => {
    await updateTask(id, { completed: !completed });
    loadTasks();
  };

  const handleDelete = async (id) => {
    if (window.confirm('¿Eliminar tarea?')) {
      await deleteTask(id);
      loadTasks();
    }
  };

  if (loading) return <p>Cargando tareas...</p>;

  return (
    <ul>
      {tasks.map(task => (
        <li key={task._id} style={{ marginBottom: '1rem' }}>
          <input
            type="checkbox"
            checked={task.completed}
            onChange={() => handleToggle(task._id, task.completed)}
          />
          <span style={{ textDecoration: task.completed ? 'line-through' : 'none', marginLeft: '0.5rem' }}>
            {task.title}
          </span>
          <button onClick={() => onEdit(task)} style={{ marginLeft: '1rem' }}>Editar</button>
          <button onClick={() => handleDelete(task._id)} style={{ marginLeft: '0.5rem' }}>Eliminar</button>
        </li>
      ))}
    </ul>
  );
}
