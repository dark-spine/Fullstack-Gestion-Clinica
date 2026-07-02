import { useState } from 'react';
import TaskList from './components/TaskList';
import TaskForm from './components/TaskForm';
import './App.css';

function App() {
  const [refresh, setRefresh] = useState(false);
  const [editingTask, setEditingTask] = useState(null);

  const handleSaved = () => {
    setRefresh(!refresh);
    setEditingTask(null);
  };

  return (
    <div className="App">
      <h1>Mis Tareas</h1>
      <TaskForm editingTask={editingTask} onSaved={handleSaved} />
      <TaskList refresh={refresh} onEdit={setEditingTask} />
    </div>
  );
}

export default App;
