#!/bin/bash

# Script de inicio rápido para el proyecto
# Ejecutar desde: entorno-desarrollo/

echo "=========================================="
echo "Sistema de Gestión de Clínicas"
echo "Arquitectura de Microservicios"
echo "=========================================="
echo ""

# Verificar requisitos
echo "✓ Verificando requisitos..."
command -v docker >/dev/null 2>&1 || { echo "Docker no instalado"; exit 1; }
command -v docker-compose >/dev/null 2>&1 || { echo "Docker Compose no instalado"; exit 1; }
command -v mvn >/dev/null 2>&1 || { echo "Maven no instalado"; exit 1; }

echo "✓ Todos los requisitos están instalados"
echo ""

# Mostrar opciones
echo "Opciones de ejecución:"
echo "1. Ejecutar con Docker Compose (RECOMENDADO)"
echo "2. Compilar proyectos localmente"
echo "3. Ejecutar pruebas unitarias"
echo "4. Ver logs en tiempo real"
echo ""

read -p "Selecciona opción (1-4): " option

case $option in
    1)
        echo "Iniciando con Docker Compose..."
        docker-compose up --build
        ;;
    2)
        echo "Compilando proyectos..."
        mvn clean package -DskipTests
        ;;
    3)
        echo "Ejecutando pruebas unitarias..."
        mvn clean test
        ;;
    4)
        echo "Mostrando logs del API Gateway..."
        docker-compose logs -f api-gateway
        ;;
    *)
        echo "Opción inválida"
        exit 1
        ;;
esac
