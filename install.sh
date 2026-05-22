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

# Save installed version
echo "$VERSION" > "${INSTALL_DIR}/version"

# Create wrapper script with built-in update and version support
cat > "${INSTALL_DIR}/spring-artisan" <<'WRAPPER'
#!/usr/bin/env bash
INSTALL_DIR="${HOME}/.spring-artisan"
REPO="Open-Spring-Labs/spring-artisan"

case "$1" in
  update)
    echo "Checking for updates..."
    LATEST=$(curl -s "https://api.github.com/repos/${REPO}/releases/latest" \
      | grep '"tag_name"' \
      | sed -E 's/.*"v([^"]+)".*/\1/')
    CURRENT=$(cat "${INSTALL_DIR}/version" 2>/dev/null || echo "unknown")
    if [ "$LATEST" = "$CURRENT" ]; then
      echo "Already on the latest version (v${CURRENT})."
    else
      echo "Updating from v${CURRENT} to v${LATEST}..."
      curl -L "https://github.com/${REPO}/releases/download/v${LATEST}/spring-artisan.jar" \
        -o "${INSTALL_DIR}/spring-artisan.jar"
      echo "$LATEST" > "${INSTALL_DIR}/version"
      echo "Updated to v${LATEST} successfully!"
    fi
    ;;
  --version|-v)
    echo "Spring Artisan v$(cat "${INSTALL_DIR}/version" 2>/dev/null || echo "unknown")"
    ;;
  *)
    java -jar "${INSTALL_DIR}/spring-artisan.jar" "$@"
    ;;
esac
WRAPPER

chmod +x "${INSTALL_DIR}/spring-artisan"

# Symlink to PATH
if [ -w "$BIN_DIR" ]; then
  ln -sf "${INSTALL_DIR}/spring-artisan" "${BIN_DIR}/spring-artisan"
else
  sudo ln -sf "${INSTALL_DIR}/spring-artisan" "${BIN_DIR}/spring-artisan"
fi

echo ""
echo "Spring Artisan v${VERSION} installed successfully!"
echo ""
echo "Commands:"
echo "  spring-artisan make model User     generate code"
echo "  spring-artisan --version           show current version"
echo "  spring-artisan update              update to latest version"
