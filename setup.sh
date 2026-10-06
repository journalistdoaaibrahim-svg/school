#!/usr/bin/env bash
# يُشغَّل مرة واحدة من داخل مجلد المشروع:  bash setup.sh
# يولّد مجلدي android/ و ios/ ويطبّق الإعدادات المطلوبة.
set -e

ORG="${1:-com.alekhlas}"
PLATFORMS="android"
# على جهاز Mac نولّد iOS أيضًا
if [ "$(uname)" = "Darwin" ]; then PLATFORMS="android,ios"; fi

flutter create --project-name school_lectures --org "$ORG" --platforms "$PLATFORMS" .
rm -f test/widget_test.dart

# أندرويد: صلاحية الإنترنت لنسخة release + اسم التطبيق + دعم RTL
cp platform_config/AndroidManifest.xml android/app/src/main/AndroidManifest.xml

# iOS: الحد الأدنى 13.0 + اسم التطبيق
if [ -f ios/Podfile ]; then
  sed -i.bak -E "s/^# *platform :ios.*/platform :ios, '13.0'/" ios/Podfile && rm -f ios/Podfile.bak
fi
if [ -x /usr/libexec/PlistBuddy ] && [ -f ios/Runner/Info.plist ]; then
  /usr/libexec/PlistBuddy -c "Set :CFBundleDisplayName مدرسة الإخلاص" ios/Runner/Info.plist || true
fi

flutter pub get
echo "تم التجهيز بنجاح."
