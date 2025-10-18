# 🚀 INTEGRACIÓN FRONTEND REACT + BACKEND SPRING BOOT

## 📋 **CONFIGURACIÓN COMPLETADA EN EL BACKEND**

### ✅ **Características Implementadas:**
- 🔐 **Autenticación JWT completa**
- 🛡️ **Spring Security configurado**
- 📦 **API REST mejorada para productos**
- 👤 **Gestión de usuarios con roles**
- 🏷️ **Sistema de categorías**
- 🛒 **Gestión de pedidos**
- 📍 **Sistema de direcciones**
- 🌐 **CORS configurado para React**

---

## 🔧 **ENDPOINTS DISPONIBLES**

### **🔐 AUTENTICACIÓN**
```http
POST /api/auth/register
POST /api/auth/login
```

### **📦 PRODUCTOS**
```http
GET    /api/productos                    # Todos los productos
GET    /api/productos/destacados         # Productos destacados
GET    /api/productos/categoria/{id}     # Productos por categoría
GET    /api/productos/buscar?q=término   # Buscar productos
GET    /api/productos/{id}               # Producto específico
POST   /api/productos                    # Crear producto (Auth)
PUT    /api/productos/{id}               # Actualizar producto (Auth)
DELETE /api/productos/{id}               # Eliminar producto (Admin)
```

### **🏷️ CATEGORÍAS**
```http
GET    /api/categorias
GET    /api/categorias/{id}
POST   /api/categorias
PUT    /api/categorias/{id}
DELETE /api/categorias/{id}
```

### **👤 USUARIOS**
```http
GET    /api/usuarios
GET    /api/usuarios/{id}
PUT    /api/usuarios/{id}
DELETE /api/usuarios/{id}
```

### **🛒 PEDIDOS**
```http
GET    /api/pedidos
GET    /api/pedidos/{id}
GET    /api/pedidos/usuario/{usuarioId}
POST   /api/pedidos
PUT    /api/pedidos/{id}
DELETE /api/pedidos/{id}
```

---

## 🎯 **CONFIGURACIÓN PARA EL FRONTEND REACT**

### **1. Variables de Entorno (.env)**
```bash
# .env.development
REACT_APP_API_URL=http://localhost:8080/api
REACT_APP_JWT_TOKEN_KEY=jwt_token

# .env.production
REACT_APP_API_URL=https://tu-backend-produccion.com/api
REACT_APP_JWT_TOKEN_KEY=jwt_token
```

### **2. Configuración de Axios (src/api/index.js)**
```javascript
import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor para agregar token JWT
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('jwt_token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Interceptor para manejar errores de autenticación
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('jwt_token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api;
```

### **3. Servicio de Autenticación (src/api/auth.js)**
```javascript
import api from './index';

export const authService = {
  login: async (credentials) => {
    const response = await api.post('/auth/login', credentials);
    if (response.data.token) {
      localStorage.setItem('jwt_token', response.data.token);
      localStorage.setItem('user', JSON.stringify(response.data));
    }
    return response.data;
  },
  
  register: async (userData) => {
    const response = await api.post('/auth/register', userData);
    if (response.data.token) {
      localStorage.setItem('jwt_token', response.data.token);
      localStorage.setItem('user', JSON.stringify(response.data));
    }
    return response.data;
  },
  
  logout: () => {
    localStorage.removeItem('jwt_token');
    localStorage.removeItem('user');
  },
  
  getCurrentUser: () => {
    return JSON.parse(localStorage.getItem('user'));
  },
  
  isAuthenticated: () => {
    return !!localStorage.getItem('jwt_token');
  }
};
```

### **4. Servicio de Productos (src/api/products.js)**
```javascript
import api from './index';

export const productService = {
  getAll: () => api.get('/productos'),
  getById: (id) => api.get(`/productos/${id}`),
  getFeatured: () => api.get('/productos/destacados'),
  getByCategory: (categoryId) => api.get(`/productos/categoria/${categoryId}`),
  search: (query) => api.get(`/productos/buscar?q=${query}`),
  create: (product) => api.post('/productos', product),
  update: (id, product) => api.put(`/productos/${id}`, product),
  delete: (id) => api.delete(`/productos/${id}`)
};
```

### **5. Context de Usuario Actualizado (src/context/UserContext.jsx)**
```javascript
import React, { createContext, useContext, useState, useEffect } from 'react';
import { authService } from '../api/auth';

const UserContext = createContext();

export const useUser = () => {
  const context = useContext(UserContext);
  if (!context) {
    throw new Error('useUser must be used within UserProvider');
  }
  return context;
};

export const UserProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const currentUser = authService.getCurrentUser();
    if (currentUser) {
      setUser(currentUser);
    }
    setLoading(false);
  }, []);

  const login = async (credentials) => {
    try {
      const userData = await authService.login(credentials);
      setUser(userData);
      return userData;
    } catch (error) {
      throw error;
    }
  };

  const register = async (userData) => {
    try {
      const response = await authService.register(userData);
      setUser(response);
      return response;
    } catch (error) {
      throw error;
    }
  };

  const logout = () => {
    authService.logout();
    setUser(null);
  };

  const value = {
    user,
    login,
    register,
    logout,
    isAuthenticated: authService.isAuthenticated(),
    loading
  };

  return (
    <UserContext.Provider value={value}>
      {children}
    </UserContext.Provider>
  );
};
```

### **6. Context de Productos Actualizado (src/context/ProductContext.jsx)**
```javascript
import React, { createContext, useContext, useState, useEffect } from 'react';
import { productService } from '../api/products';

const ProductContext = createContext();

export const useProducts = () => {
  const context = useContext(ProductContext);
  if (!context) {
    throw new Error('useProducts must be used within ProductProvider');
  }
  return context;
};

export const ProductProvider = ({ children }) => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchProducts = async () => {
    setLoading(true);
    try {
      const response = await productService.getAll();
      setProducts(response.data);
      setError(null);
    } catch (err) {
      setError('Error al cargar productos');
      console.error('Error fetching products:', err);
    } finally {
      setLoading(false);
    }
  };

  const searchProducts = async (query) => {
    setLoading(true);
    try {
      const response = await productService.search(query);
      setProducts(response.data);
      setError(null);
    } catch (err) {
      setError('Error en la búsqueda');
      console.error('Error searching products:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchProducts();
  }, []);

  const value = {
    products,
    loading,
    error,
    fetchProducts,
    searchProducts
  };

  return (
    <ProductContext.Provider value={value}>
      {children}
    </ProductContext.Provider>
  );
};
```

---

## 🚀 **PASOS PARA EJECUTAR**

### **Backend (Spring Boot):**
```bash
cd ecommerce-backend
./mvnw spring-boot:run
# Ejecuta en http://localhost:8080
```

### **Frontend (React):**
```bash
cd ecommerce-frontend
npm install
npm start
# Ejecuta en http://localhost:3000
```

---

## 📋 **ESTRUCTURA DE DATOS**

### **Usuario Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "id": 1,
  "email": "user@example.com",
  "nombre": "Juan",
  "apellido": "Pérez",
  "role": "USER"
}
```

### **Producto Response:**
```json
{
  "id": 1,
  "nombre": "iPhone 15 Pro",
  "descripcion": "Smartphone Apple iPhone 15 Pro 128GB",
  "precio": 1299.99,
  "stock": 25,
  "imagen": "https://example.com/image.jpg",
  "activo": true,
  "destacado": false,
  "marca": "Apple",
  "categorias": ["Electrónicos"]
}
```

---

## ⚡ **PRÓXIMOS PASOS**

1. **Reemplazar las APIs mock** en tu frontend con las configuraciones de arriba
2. **Actualizar tus contextos** con los códigos proporcionados
3. **Configurar las variables de entorno**
4. **Probar la autenticación** con login/register
5. **Verificar la integración** de productos y carrito

¡Tu backend está completamente listo para integrarse con React! 🎉