# تطبيق المحاضرات التعليمية — مدرسة الإخلاص

التطبيق: شاشة ترحيب ← قائمة المحاضرات ← مشاهدة الفيديو داخل التطبيق.
لا حسابات، لا قاعدة بيانات، لا سيرفر. الإنترنت مطلوب فقط لتشغيل الفيديو.

---

## أسهل طريقة للحصول على ملف APK (بدون تثبيت أي برنامج)

1. أنشئ حسابًا مجانيًا على **github.com**.
2. اضغط **New repository** ← اختر **Private** ← أنشئه.
3. اضغط **uploading an existing file** وارفع **كل** محتويات هذا المجلد (بما فيها المجلد المخفي `.github`) ثم **Commit**.
   > تأكد أن اسم الفرع `main`.
4. افتح تبويب **Actions** ← اختر **Build Android APK** ← **Run workflow**.
5. انتظر 5 إلى 8 دقائق حتى تظهر علامة ✓ خضراء.
6. افتح التشغيل المكتمل ← من الأسفل نزّل **alekhlas-apk** ← فك الضغط ← ستجد `app-release.apk`.
7. أرسله للطلاب. على هاتف أندرويد: افتح الملف ← اسمح بـ «التثبيت من مصادر غير معروفة» ← تثبيت.

---

## 1) تشغيل المشروع على جهازك (للمبرمجين)

المطلوب: Flutter SDK (https://docs.flutter.dev/get-started/install).

```bash
bash setup.sh          # مرة واحدة: يولّد android/ و ios/ ويضبط الإعدادات
flutter run            # تشغيل على هاتف موصول أو محاكي
```

## 2) استبدال شعار المدرسة

الشعار الحالي هو شعار مدرسة الإخلاص في الملف `assets/logo.jpg`.
لاستبداله بشعار آخر: احذف الملف وضع شعارك الجديد **بنفس الاسم والامتداد** `logo.jpg`.
(إن كان شعارك PNG: سمّه `logo.png`، ثم غيّر السطر `kLogoAsset` في `lib/constants/app_theme.dart`
وغيّر اسم الملف في `pubspec.yaml` تحت `assets:`).

## 3) تغيير ألوان الهوية

افتح `lib/constants/app_theme.dart` وعدّل الألوان في الأعلى:

```dart
static const blue   = Color(0xFF0B2BE0);  // الأزرق
static const orange = Color(0xFFFF6A00);  // البرتقالي
static const red    = Color(0xFFE31B1B);  // الأحمر
```
اكتب الكود بصيغة `0xFF` ثم رمز اللون الستة أحرف (مثلًا `#0B2BE0` تصبح `0xFF0B2BE0`).

## 4) إضافة محاضرة جديدة

افتح الملف الوحيد `lib/constants/lectures.dart` وأضف سطرًا:

```dart
const List<Lecture> kLectures = [
  Lecture(title: 'محاضرة قوانين السير', videoId: 'rMVZwpDlARo'),
  Lecture(title: 'محاضرة الميكانيكا',   videoId: 'sKbqPG386-c'),
  Lecture(title: 'محاضرة الإشارات',     videoId: 'P_EMWF63d_o'),
  Lecture(title: 'عنوان المحاضرة الجديدة', videoId: 'معرّف_الفيديو'),   // ← جديد
];
```
`videoId` هو الجزء بعد `youtu.be/` في الرابط (11 حرفًا).
بعد التعديل أعد بناء الـ APK وأرسله للطلاب (المحاضرات داخل التطبيق، فتحتاج إصدارًا جديدًا).

لتعديل أي نص في التطبيق (اسم المدرسة، العبارات...): `lib/constants/strings.dart`.

## 5) بناء APK لأندرويد على جهازك

```bash
flutter build apk --release
```
الناتج: `build/app/outputs/flutter-apk/app-release.apk`
(موقّع بمفتاح debug، يصلح للتوزيع المباشر. للنشر على Google Play تحتاج مفتاح توقيع خاصًا.)

## 6) بناء iOS (يحتاج جهاز Mac + Xcode)

```bash
bash setup.sh
cd ios && pod install && cd ..
flutter build ipa --release
```
أو افتح `ios/Runner.xcworkspace` في Xcode ← Signing & Capabilities ← اختر Team ← Product ← Archive.
تثبيت التطبيق على iPhone يتطلب حساب **Apple Developer** (99$ سنويًا)، ويوزَّع عبر TestFlight أو Ad Hoc.

---

## ملاحظات
- الفيديو يعمل عبر YouTube IFrame Player API الرسمي (حزمة `youtube_player_iframe`) بأدوات التحكم الرسمية من YouTube كما هي، دون أي تعديل عليها.
  لذلك قد يظهر شعار YouTube وعنوان الفيديو داخل المشغّل، وهذا من سياسات YouTube ولا يمكن إخفاؤه.
- الفيديوهات يجب أن يكون **التضمين (Embedding) مسموحًا** لها في YouTube Studio ← تفاصيل الفيديو ← «السماح بالتضمين».
- بدون إنترنت تظهر رسالة: «يحتاج تشغيل المحاضرة إلى اتصال بالإنترنت.»
