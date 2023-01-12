
import './App.scss'
import React from 'react'
import KubernetesPage from './paginas/KubernetesPage'
import { Navbar } from './componentes/Navbar'
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import HomePage from './paginas/HomePage';
import ShellPage from './paginas/ShellPage';
import DocumentacionPage from './paginas/DocumentacionPage';
import KeitsFormularioPage from './paginas/KeitsFormularioPage';
import ProjectPage from './paginas/ProjectPage';
import Credentials from './paginas/Credentials';
import CredentialsEdit from './paginas/CredentialsEdit';
import ConsoleLogs from './paginas/ConsoleLogs';

function App() {
  return (
    <Router>
        <Navbar />
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/home" element={<HomePage />} />
          <Route path="/kubernetes" element={<KubernetesPage />} />
          <Route path="/projects" element={<ProjectPage />} />
          <Route path="/shell" element={<ShellPage />} />
          <Route path="/documentacion" element={<DocumentacionPage />} />
          <Route path="/kubernetes/formulario" element={<KeitsFormularioPage />} />
          <Route path="/credentials" element={<Credentials />} />
          <Route path="/credentials/edits" element={<CredentialsEdit />} />
          <Route path="/consoleLogs" element={<ConsoleLogs />} />
        </Routes>
    </Router>
  );
}

export default App;
