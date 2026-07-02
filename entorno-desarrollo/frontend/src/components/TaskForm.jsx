import { useState, useEffect } from 'react';
import { createTask, updateTask } from '../services/api';

export default function TaskForm({ editingTask, onSaved }) {
  const [title, setTitle] = useState('');

  useEffect(() => {
    if (editingTask) {
      setTitle(editingTask.title);
    } else {
      setTitle('');
    }
  }, [editingTask]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!title.trim()) return;
    if (editingTask) {
      await updateTask(editingTask._id, { title, completed: editingTask.completed });
    } else {
      await createTask({ title, completed: false });
    }
    setTitle('');
    onSaved();
  };

  return (
    <form onSubmit={handleSubmit}>
      <input
        type="text"
        placeholder="Nueva tarea..."
        value={title}
        onChange={(e) => setTitle(e.target.value)}
        required
      />
      <button type="submit">{editingTask ? 'Actualizar' : 'Agregar'}</button>
      {editingTask && (
        <button type="button" onClick={() => onSaved()}>Cancelar</button>
      )}
    </form>
  );
}
