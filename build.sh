#!/bin/bash

# Build script for PoorPlugin

echo "========================================="
echo "Building PoorPlugin..."
echo "========================================="

# Проверяем наличие Maven
if ! command -v mvn &> /dev/null; then
    echo "Maven not found! Install Maven first."
    exit 1
fi

# Очищаем и собираем
mvn clean package

if [ $? -eq 0 ]; then
    echo ""
    echo "========================================="
    echo "✓ Build successful!"
    echo "✓ Plugin ready: target/poorplugin-1.0.0.jar"
    echo "========================================="
    
    # Копируем в папку plugins если она есть
    if [ -d "plugins" ]; then
        cp target/poorplugin-1.0.0.jar plugins/
        echo "✓ Plugin copied to plugins/ folder"
    fi
else
    echo ""
    echo "========================================="
    echo "✗ Build failed!"
    echo "========================================="
    exit 1
fi
