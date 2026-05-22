#!/usr/bin/env bash
set -e

REPO="Open-Spring-Labs/spring-artisan"
INSTALL_DIR="${HOME}/.spring-artisan"
BIN_DIR="/usr/local/bin"

echo "Installing Spring Artisan..."

# Get latest release version
VERSION=$(curl -s "https://api.github.com/repos/${REPO}/releases/latest" \
  | grep '"tag_name"' \
  | sed -E 's/.*"v([^"]+)".*/\1/')

if [ -z "$VERSION" ]; then
  echo "Error: Could not determine latest version." >&2
  exit 1
fi

echo "Latest version: v${VERSION}"

# Download JAR
mkdir -p "$INSTALL_DIR"
JAR_URL="https://github.com/${REPO}/releases/download/v${VERSION}/spring-artisan.jar"
curl -L "$JAR_URL" -o "${INSTALL_DIR}/spring-artisan.jar"

# Create wrapper script
cat > "${INSTALL_DIR}/spring-artisan" <<'EOF'
#!/usr/bin/env bash
java -jar "${HOME}/.spring-artisan/spring-artisan.jar" "$@"
EOF
chmod +x "${INSTALL_DIR}/spring-artisan"

# Symlink to PATH
if [ -w "$BIN_DIR" ]; then
  ln -sf "${INSTALL_DIR}/spring-artisan" "${BIN_DIR}/spring-artisan"
else
  sudo ln -sf "${INSTALL_DIR}/spring-artisan" "${BIN_DIR}/spring-artisan"
fi

echo ""
echo "Spring Artisan v${VERSION} installed successfully!"
echo "Run: spring-artisan make model User"
