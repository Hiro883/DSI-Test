import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./views/Login";
import HomeDashboard from "./views/HomeDashboard";
import DentalDashboard from "./views/DentalDashboard";
import ScheduleAppointments from "./views/ScheduleAppointments";
import PatientManagement from "./views/PatientManagement";
import ProtectedRoute from "./components/ProtectedRoute";
import Layout from "./components/Layout";
import UserManagement from "./views/UserManagement";
import ConsultaIndex from "./views/ConsultaIndex";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Ruta publica */}
        <Route path="/" element={<Login />} />

        {/* Rutas privadas dentro del Layout */}
        <Route
          element={
            <ProtectedRoute>
              <Layout />
            </ProtectedRoute>
          }
        >
          {/* Desde esta parte es donde se autentifican los roles para que deje visualizar ciertas pantallas,
          las pantallas estan en el apartado de "path" */}

          {/*Acceso a todos los roles autenticados */}
          <Route path="/dashboard" element={<HomeDashboard />} />

          {/* Acceso solo a ADMIN y ODONTOLOGO */}
          <Route
            path="/consulta"
            element={
              <ProtectedRoute allowedRoles={["ADMIN", "ODONTOLOGO"]}>
                <ConsultaIndex />
              </ProtectedRoute>
            }
          />
          <Route
            path="/consulta/:citaId"
            element={
              <ProtectedRoute allowedRoles={["ADMIN", "ODONTOLOGO"]}>
                <DentalDashboard />
              </ProtectedRoute>
            }
          />
          <Route
            path="/pacientes"
            element={
              <ProtectedRoute allowedRoles={["ADMIN", "ODONTOLOGO"]}>
                <PatientManagement />
              </ProtectedRoute>
            }
          />

          {/* Acceso solo a ADMIN y RECEPCIONISTA */}
          <Route
            path="/agenda"
            element={
              <ProtectedRoute allowedRoles={["ADMIN", "RECEPCIONISTA"]}>
                <ScheduleAppointments />
              </ProtectedRoute>
            }
          />

          {/* Acceso solo a ADMIN */}
          <Route
            path="/usuarios"
            element={
              <ProtectedRoute allowedRoles={["ADMIN"]}>
                <UserManagement />
              </ProtectedRoute>
            }
          />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;