# MyOS - نظام إدارة الحياة والعقل الثاني (Android / Jetpack Compose)

> **دليل الذكاء الاصطناعي والمطورين (AI Context & Architecture Primer)**:
> هذا الملف مُعد خصيصاً ليكون المرجع الشامل والوحيد المطلوب لأي نموذج ذكاء اصطناعي (أو مطور) لمواصلة تطوير المشروع فوراً وبدون استهلاك كوتا في قراءة وتحليل كافة ملفات الكود المصدرية. يُرجى تحديث هذا الملف بعد كل ميزة أو تعديل جديد.

---

## 1. الهوية والتقنيات (Project Identity & Tech Stack)
- **اسم التطبيق**: MyOS (نظام التشغيل الشخصي الذكي لإدارة الحياة والإنتاجية).
- **المنصة**: Android (Kotlin, Jetpack Compose, Material Design 3).
- **إعدادات البناء**:
  - `compileSdk`: 35 | `minSdk`: 24 | `targetSdk`: 35 | Kotlin: 2.0.21 | Java: 17.
  - المعمارية: **MVVM** مع `StateFlow` و `Coroutines` ومستودع بيانات تفاعلي `MyOSRepository`.
  - واجهة المستخدم: تدعم اللغة العربية واتجاه اليمين لليسار بالكامل (`LayoutDirection.Rtl`).
  - ألوان التطبيق: Material 3 بألوان زاهية ومنسقة (الأزرق الساطع `BrightBlue`، الزمردي للعادات `HabitEmerald`، البنفسجي العصري `ElectricViolet`).

---

## 2. المرحلة الحالية وما تم إنجازه (Current Milestone Status)

### أ. لوحة التحكم الرئيسية (`HomeScreen`)
- بطاقة ترحيب ذكية (`WelcomeCard`) مع اسم المستخدم والجملة التحفيزية التفاعلية وزر التعديل السريع.
- بطاقة الإحصائيات التحليلية لليوم (`DailySummarySection`): العادات المكتملة، المهام، ومهام الأهداف لليوم.
- قسم "عليك اليوم" (`TodaySection`):
  - **العادات**: يظهر فقط عادات اليوم المجدولة بناءً على أيام الأسبوع.
  - **وضع الراحة في الرئيسية**: عند تفعيل زر "راحة ☕"، تختفي العادات غير الإجبارية ويظهر تنبيه لطيف بأن اليوم راحة مستحقة، بينما تظل **العادات الإجبارية 🛡️** (مثل الصلوات والأذكار) ظاهرة ونشطة.
  - **الأهداف**: تعرض فقط الأهداف غير المكتملة (`progressPercentage < 100`) وغير المتوقفة مؤقتاً، مع قائمة منسدلة مستقلة لكل هدف تعرض مهامه المجدولة لليوم فقط. الأهداف المكتملة 100% تختفي تلقائياً من الرئيسية.
  - **المهام العامة**: قائمة مهام اليوم العامة مع خانات إكمال تفاعلية.

### ب. نظام الأهداف الكامل (`GoalsScreen` & `GoalDetailsScreen`)
- **صفحة الأهداف (`GoalsScreen`)**:
  - فلاتر الأهداف: الكل (`ALL`)، النشطة (`ACTIVE`)، المتوقفة مؤقتاً (`PAUSED`)، المكتملة (`COMPLETED`).
  - بطاقة ملخص الهدف (`GoalOverviewCard`): اسم الهدف، نسبة التقدم المتحركة، شريط التقدم، عدد المهام، الأيقونة المخصصة.
  - زر الإضافة السريعة وإمكانية إنشاء هدف جديد عبر نافذة سفلية (`CreateGoalBottomSheet`).
- **صفحة تفاصيل الهدف (`GoalDetailsScreen`)**:
  - عرض تفصيلي دائري وشريطي للنسبة المئوية.
  - زر ركن/إيقاف مؤقت للهدف وزر استئنافه.
  - **إمكانية حذف الهدف**: زر حذف مخصص في رأس البطاقة وفي تبويب التفاصيل مع حوار تأكيد تحذيري (`AlertDialog`) لحذف الهدف وكافة مهامه.
  - تبويبات داخلية: (مهام الهدف، تفاصيل ومعلومات الهدف، إحصائيات الإنجاز).
  - إضافة، تعديل، حذف مهام الهدف مع ترتيب أولويات المهام (عالية -> متوسطة -> منخفضة).

### ج. نظام وصفحة العادات الشاملة (`HabitsScreen` - تم إنجازها حديثاً)
- **شاشة العادات المخصصة (`HabitsScreen`)**:
  - ترتبط بشريط التنقل السفلي والقائمة الجانبية (`ScreenDestination.HABITS`).
  - بطاقة علوية بإحصائيات اليوم (عادات اليوم المكتملة، العادات الإجبارية، حالة وضع الراحة).
  - فلاتر تفاعلية: (اليوم، كل العادات، إجبارية 🛡️، دورية / أسبوعية 📅).
  - بطاقة كل عادة (`HabitDetailCard`): أيقونة العادة، اسمها، عدد أيام الاستمرار (Streak 🔥)، جدول الأيام، أزرار تعديل وحذف، وزر تفاعلي مباشر (+1، أو تسجيل كمية/وقت، أو زر تأكيد).
- **أنواع وطرق قياس العادات الأربعة (`HabitType`)**:
  1. `BOOLEAN`: تأكيد لمرة واحدة يومياً (مثل أذكار الصباح).
  2. `QUANTITY`: كمية / عدد مع **إمكانية تسمية وحدة القياس بحرية** (مثل: "صفحات" لورد القرآن، "عدات" للضغط، "نطات" للحبل، "كلمات"، "كيلومتر").
  3. `DURATION`: وقت ومدة بالدقائق (مثل: 30 دقيقة تدرب كيبورد، 60 دقيقة جيم).
  4. `COUNTER`: تكرار وضغطات مع تسمية الوحدة (مثل: 5 "صلوات"، 8 "أكواب" ماء).
- **الجدولة وأيام الأسبوع (`HabitFrequency` & `DayOfWeekArabic`)**:
  - إما يومية (`DAILY`) أو بأيام محددة في الأسبوع (`SPECIFIC_DAYS`) مثل الجيم (سبت، اثنين، خميس).
- **التفاعل الذكي مع وضع الراحة**:
  - خيار `isMandatory = true`: عادة إجبارية لا تتأثر بوضع الراحة وتظل نشطة في الرئيسية (كالصلوات).
  - خيار `isMandatory = false`: عادة قابلة للراحة؛ عند تفعيل وضع الراحة تُسجل كيوم راحة مستحق لحماية الـ Streak وتختفي من الصفحة الرئيسية.
- **منتقي الأيقونات التعبيري (`Icon Picker`)**:
  - شبكة أيقونات مخصصة (🕌, 💧, 📖, 🏋️‍♂️, ⌨️, 💪, 🏃‍♂️, 🧘‍♂️, 💊, 🌙, 📚, 💻, 🥗, ✍️, 🧹, 🌱).

---

## 3. المعمارية وتدفق البيانات (Architecture & State Flow)

```
[MainActivity] (Single Activity Routing)
      │
      ├──> [HomeScreen] (TodaySection, DailySummarySection, WelcomeCard)
      ├──> [GoalsScreen] & [GoalDetailsScreen] (Goal management & tasks)
      ├──> [HabitsScreen] (Habit list, filters, logging dialogs)
      └──> NavigationDrawer & BottomNavBar (ScreenDestination routing)
               ▲
               │ uses StateFlow<MyOSUiState> & ViewModel actions
               ▼
       [MyOSViewModel]
               ▲
               │ updates & reads
               ▼
       [MyOSRepository] (In-memory reactive single source of truth)
```

### القواعد البرمجية الصارمة للمشروع:
1. **قاعدة إخفاء الأهداف المكتملة من الرئيسية**:
   - `TodaySection` يصفي الأهداف النشطة دائماً كالتالي:
     `goals.filter { !it.isPaused && it.progressPercentage < 100 }`
   - الهدف البالغ 100% يختفي من الرئيسية ويظل متاحاً في تبويب الأهداف تحت "المكتملة" و"الكل".
2. **قاعدة وضع الراحة والعادات**:
   - عند `isRestModeActive == true`، تظهر فقط العادات التي ينطبق عليها `habit.isMandatory == true`.
   - العادات غير الإجبارية تختفي من الرئيسية وتُعرض في صفحة العادات مع شارة "يوم راحة مستحق ☕".
   - أيام الأسبوع تُفحص دائماً عبر دالة `getCurrentDayOfWeekArabic()`.
3. **أولويات المهام**:
   - مهام الأهداف ترتب تلقائياً بحسب الأولوية: `HIGH` أولاً ثم `MEDIUM` ثم `LOW`.
4. **زر الرجوع (`BackHandler`)**:
   - أي شاشة فرعية أو تفاصيل هدف تحتوي على `BackHandler` لإعادة المستخدم للشاشة السابقة أو الرئيسية بأمان.

---

## 4. نماذج البيانات الأساسية (Data Models Reference)

### نموذج العادة (`Habit.kt`)
```kotlin
data class Habit(
  val id: String,
  val title: String,
  val type: HabitType = HabitType.BOOLEAN, // BOOLEAN, QUANTITY, DURATION, COUNTER
  val targetValue: Int = 1,
  val currentValue: Int = 0,
  val unit: String = "مرة",                // صفحات، عدات، نطات، دقيقة، صلوات، أكواب...
  val frequency: HabitFrequency = HabitFrequency.DAILY, // DAILY أو SPECIFIC_DAYS
  val scheduledDays: List<DayOfWeekArabic> = emptyList(), // السبت، الإثنين...
  val isMandatory: Boolean = false,       // إجبارية لا تتأثر بوضع الراحة
  val priority: Priority = Priority.HIGH,
  val currentStreak: Int = 12,
  val iconEmoji: String = "🌱"
)
```

### نموذج الهدف (`Goal.kt`)
```kotlin
data class Goal(
  val id: String,
  val title: String,
  val description: String? = null,
  val iconId: String = "target",
  val priority: Priority = Priority.HIGH,
  val status: GoalStatus = GoalStatus.ACTIVE, // ACTIVE, PAUSED, COMPLETED
  val dueDate: String? = "2025/12/31",
  val isPaused: Boolean = false,
  val tasks: List<Task> = emptyList()
)
```

### نموذج المهمة (`Task.kt`)
```kotlin
data class Task(
  val id: String,
  val title: String,
  val priority: Priority = Priority.MEDIUM,
  val isCompleted: Boolean = false,
  val isGoalTask: Boolean = false,
  val schedule: TaskSchedule = TaskSchedule.TODAY, // TODAY, UPCOMING, SOMEDAY
  val notes: String? = null,
  val dueDateFormatted: String? = null
)
```

---

## 5. خريطة ملفات المشروع ومسؤولياتها (File Directory Map)

- `/app/src/main/java/com/example/`
  - `MainActivity.kt`: نقطة الانطلاق الرئيسية، إدارة التوجيه بين الشاشات بناءً على `uiState.currentScreen` وتفاصيل الهدف المحدد.
  - **`model/`**:
    - `Habit.kt`: نموذج العادة، `HabitType`، `HabitFrequency`، `DayOfWeekArabic`، ودالة `getCurrentDayOfWeekArabic()`.
    - `Goal.kt`: نموذج الهدف، `GoalStatus`.
    - `Task.kt`: نموذج المهمة، `TaskSchedule`.
    - `Priority.kt`: درجات الأولوية (HIGH, MEDIUM, LOW, NONE) وألوانها.
    - `User.kt`: بيانات المستخدم وحالة وضع الراحة `isRestModeActive`.
    - `DailyAnalytics.kt`: حساب إنجازات اليوم.
    - `GoalIconItem.kt`: أيقونات الأهداف وخلفياتها الملونة.
    - `NavigationItem.kt`: عناصر القائمة الجانبية والشريط السفلي `ScreenDestination`.
    - `QuickAddOption.kt`: خيارات زر الإضافة السريع.
  - **`data/`**:
    - `MyOSRepository.kt`: المستودع المركزي التفاعلي لإدارة وحفظ بيانات العادات والأهداف والمهام وحساب التحليلات ووضع الراحة.
  - **`viewmodel/`**:
    - `MyOSViewModel.kt`: وسيط الحالة، استقبال تفاعلات المستخدم وتحديث المستودع وتنبيهات الـ Snackbar.
    - `MyOSUiState.kt`: كائن الحالة الشامل لواجهة المستخدم وفلاتر العادات والأهداف.
  - **`ui/screens/`**:
    - `HomeScreen.kt`: الشاشة الرئيسية، الترحيب، الإحصائيات، وقسم عليك اليوم.
    - `GoalsScreen.kt`: شاشة استعراض الأهداف، التصنيفات، والبطاقات الملخصة.
    - `GoalDetailsScreen.kt`: تفاصيل الهدف، إدارته، حذفه، إيقافه مؤقتاً، ومهامه التابعة.
    - `HabitsScreen.kt`: شاشة العادات الكاملة، الإحصائيات، الفلاتر، بطاقات التفاعل، وحوارات التسجيل والحذف.
    - `PlaceholderScreen.kt`: شاشة افتراضية للأقسام الجاري تطويرها مع زر عودة للرئيسية.
  - **`ui/components/`**:
    - `TodaySection.kt`: قسم "عليك اليوم" في الرئيسية مع فلترة العادات بوضع الراحة وإخفاء الأهداف المكتملة 100%.
    - `CreateHabitBottomSheet.kt`: النافذة السفلية لإنشاء وتعديل العادات بجميع حقولها وطرق حسابها وتسمية وحداتها.
    - `HabitDetailCard.kt`: بطاقة العادة التفصيلية مع أزرار التسجيل التفاعلية والتعديل والحذف.
    - `GoalOverviewCard.kt`: بطاقة عرض ملخص الهدف ونسبة إنجازه.
    - `CreateGoalBottomSheet.kt`: النافذة السفلية لإنشاء وتعديل الأهداف.
    - `AddGoalTaskDialog.kt`: حوار إضافة وتعديل مهمة تابعة لهدف مع تحديد موعدها وأولويتها.
    - `LogHabitProgressDialog.kt`: حوار تسجيل كمية أو وقت العادات ذات الأرقام.
    - `DailySummarySection.kt`: بطاقات ملخص إنجازات اليوم الثلاثية.
    - `WelcomeCard.kt`: بطاقة التحية التفاعلية مع خيار تعديل الاسم والجملة التحفيزية.
    - `MyOSHeader.kt`: الشريط العلوي، شعار التطبيق، زر القائمة الجانبية، وزر تفعيل وإلغاء وضع الراحة `راحة`.
    - `MyOSBottomNavigationBar.kt`: شريط التنقل السفلي المطور.
    - `NavigationDrawerContent.kt`: القائمة الجانبية الكاملة لجميع أقسام التطبيق.
- `/app/src/test/java/com/example/`
  - `ExampleRobolectricTest.kt`: اختبارات وحدة كاملة تختبر منطق الأهداف، العادات، التكرارات، التواريخ، وضع الراحة، وفلترة الإنجاز 100%.

---

## 6. الخطوات القادمة للمشروع (Upcoming Roadmap)
1. **صفحة المهام الكاملة (`TasksScreen`)**:
   - تنظيم المهام بحسب الموعد (اليوم، القادمة، يوماً ما) والمشاريع والقوائم وتحديد الأولويات.
2. **المشاريع والقوائم (`Projects & Lists`)**:
   - ربط المهام بمشاريع رئيسية وقوائم فرعية.
3. **التقويم والجدول الزمني (`Calendar & Timeline`)**.
4. **الملاحظات وقاعدة المعرفة (`Notes & Knowledge`)**.
5. **شاشة التركيز (`Focus Mode / Pomodoro`)**.
6. **التحليلات المتقدمة والتقارير الأسبوعية والشهرية (`Analytics Screen`)**.

---
*تم إعداد هذا الملف تلقائياً وتحديثه مع كل خطوة تطوير لضمان استمرارية برمجية سلسة وخالية من الهدر.*
