#!/usr/bin/env bash
# =============================================================================
# fix_repo.sh
# Run this ONCE from your repo root to fix all CI errors:
#   1. Renames Logintest.java → LoginTest.java  (Java class name must match file)
#   2. Verifies config.properties exists
#   3. Stages the rename properly via git so GitHub sees the correct case
# =============================================================================

set -e
REPO_ROOT="$(cd "$(dirname "$0")" && pwd)"
TEST_DIR="$REPO_ROOT/src/main/java/com/dams/test"

echo "============================================"
echo "  DAMS Selenium – Repo Fix Script"
echo "============================================"
echo ""

# ── Fix 1: Logintest.java → LoginTest.java ───────────────────────────────────
WRONG="$TEST_DIR/Logintest.java"
RIGHT="$TEST_DIR/LoginTest.java"

if [ -f "$WRONG" ]; then
    echo "[FIX 1] Renaming Logintest.java → LoginTest.java"
    # Use git mv so git tracks the rename correctly on case-insensitive filesystems
    git mv "$WRONG" "$RIGHT" 2>/dev/null || mv "$WRONG" "$RIGHT"
    echo "        Done."
elif [ -f "$RIGHT" ]; then
    echo "[OK]    LoginTest.java already has the correct name."
else
    echo "[WARN]  Neither Logintest.java nor LoginTest.java found in $TEST_DIR"
    echo "        Please check your test directory."
fi

# ── Fix 2: Verify config.properties exists ───────────────────────────────────
CONFIG="$REPO_ROOT/src/main/resources/config.properties"
if [ -f "$CONFIG" ]; then
    echo "[OK]    config.properties found at src/main/resources/"
else
    echo "[ERROR] config.properties NOT found at $CONFIG"
    echo "        Create it with at minimum:"
    echo "            browser=chrome"
    echo "            headless=false"
    echo "            url=https://devadmin.damsdelhi.com/"
    echo "            email=07siddwivedi@gmail.com"
    echo "            password=Siddharth@123"
    echo "            otp=1980"
    echo "            implicit.wait=10"
    echo "            explicit.wait=20"
    echo "            page.load.timeout=30"
    echo "            report.path=test-output/reports/DAMS_Report.html"
    echo "            screenshot.path=test-output/screenshots/"
    exit 1
fi

# ── Fix 3: Show git status ────────────────────────────────────────────────────
echo ""
echo "Git status after fixes:"
git status --short

echo ""
echo "============================================"
echo "  Next steps:"
echo "    git add ."
echo "    git commit -m 'fix: rename Logintest.java to LoginTest.java'"
echo "    git push origin main"
echo "============================================"
