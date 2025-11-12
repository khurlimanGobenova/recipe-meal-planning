import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import './index.css';          // global styles
import StartPage from './StartPage.jsx'; // import your component

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <StartPage />
  </StrictMode>
);
