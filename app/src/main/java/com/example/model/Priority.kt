package com.example.model

enum class Priority(val titleArabic: String, val titleEnglish: String, val rank: Int) {
  HIGH("عالية", "High", 1),
  MEDIUM("متوسطة", "Medium", 2),
  LOW("منخفضة", "Low", 3),
  NONE("بدون أولوية", "None", 4);

  constructor(titleArabic: String, rank: Int) : this(titleArabic, titleArabic, rank)

  fun getTitle(lang: AppLanguage): String = when (lang) {
    AppLanguage.ARABIC -> titleArabic
    AppLanguage.ENGLISH -> titleEnglish
  }
}
