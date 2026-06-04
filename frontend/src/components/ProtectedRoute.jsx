import React from 'react';
import { Navigate } from 'react-router-dom';

/**
 * Decodifica el payload de un JWT sin libreria externa.
 * No valida la firma (eso es tarea del servidor). Solo verifica expiracion en cliente.
 */
const isTokenValid = (token) => {
  if (!token) return false;
  try {
    // El JWT tiene 3 partes separadas por puntos: header.payload.signature
    const payloadBase64 = token.split('.')[1];
    const payload = JSON.parse(atob(payloadBase64));
    // "exp" esta en segundos Unix; Date.now() esta en milisegundos
    return payload.exp * 1000 > Date.now();
  } catch {
    // Si el token esta malformado, lo consideramos invalido
    return false;
  }
};

/**
 * Extrae el claim "rol" del payload del JWT.
 */
const getRolFromToken = (token) => {
  try {
    const payloadBase64 = token.split('.')[1];
    const payload = JSON.parse(atob(payloadBase64));
    return payload.rol ?? null; // "rol" es el claim que usa JwtUtil.java
  } catch {
    return null;
  }
};

/**
 * allowedRoles: array de roles permitidos, ej: ["ADMIN", "DENTIST"]
 * Si no se pasa allowedRoles, solo verifica que el token sea valido.
 */
const ProtectedRoute = ({ children, allowedRoles }) => {
  const token = localStorage.getItem('authToken');
  const authenticated = isTokenValid(token);

  if (!authenticated) {
    localStorage.clear();
    return <Navigate to="/" replace />;
  }

  if (allowedRoles && allowedRoles.length > 0) {
    const rol = getRolFromToken(token);
    if (!allowedRoles.includes(rol)) {
      // Token valido pero el rol no tiene permiso para esta ruta
      return <Navigate to="/dashboard" replace />;
    }
  }

  return children;
};

export default ProtectedRoute;