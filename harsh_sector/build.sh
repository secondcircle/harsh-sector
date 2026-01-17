#!/bin/bash
# Build script for Harsh Sector mod
# Run from the harsh_sector directory: ./build.sh

set -e  # Exit on error

# Paths
JAVA_HOME="/opt/homebrew/opt/openjdk@17"
STARSECTOR_JAVA="/Applications/Starsector.app/Contents/Resources/Java"
STARSECTOR_API="$STARSECTOR_JAVA/starfarer.api.jar"
LOG4J="$STARSECTOR_JAVA/log4j-1.2.9.jar"

# LunaLib (soft dependency - required for compilation, optional at runtime)
LUNALIB_JAR="/Applications/Starsector.app/mods/LunaLib/jars/LunaLib.jar"
if [ ! -f "$LUNALIB_JAR" ]; then
    echo "ERROR: LunaLib not found at $LUNALIB_JAR"
    echo "Install LunaLib to compile. Download from: https://github.com/Lukas22041/LunaLib/releases"
    exit 1
fi

CLASSPATH="$STARSECTOR_API:$LOG4J:$LUNALIB_JAR"
SRC_DIR="src"
BUILD_DIR="build"
JAR_DIR="jars"
JAR_NAME="HarshSector.jar"

# Clean and create build directory
rm -rf "$BUILD_DIR"
mkdir -p "$BUILD_DIR"
mkdir -p "$JAR_DIR"

echo "=== Compiling Java files ==="
# Compile with Java 7 compatibility (Starsector requirement)
# Note: --release 7 ensures compatibility with Starsector's Java version
"$JAVA_HOME/bin/javac" \
    -cp "$CLASSPATH" \
    -d "$BUILD_DIR" \
    --release 7 \
    "$SRC_DIR"/harshsector/*.java

echo "=== Creating JAR ==="
# Create JAR file from compiled classes
"$JAVA_HOME/bin/jar" cf "$JAR_DIR/$JAR_NAME" -C "$BUILD_DIR" .

echo "=== Done ==="
echo "JAR created: $JAR_DIR/$JAR_NAME"
ls -la "$JAR_DIR/$JAR_NAME"
