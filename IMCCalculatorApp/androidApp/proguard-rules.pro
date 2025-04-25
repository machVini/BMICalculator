# Regras gerais
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

# Regras para Kotlin
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

# Regras para Jetpack Compose
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Regras para AdMob
-keep class com.google.android.gms.ads.** { *; }
-dontwarn com.google.android.gms.ads.**

# Regras para Firebase
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# Mantenha suas classes de modelo
-keep class com.mach.apps.imccalculatorapp.** { *; }

# Regras para Serialização (se você estiver usando)
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.SerializationKt
-keep,includedescriptorclasses class com.mach.apps.imccalculatorapp.**$$serializer { *; }
-keepclassmembers class com.mach.apps.imccalculatorapp.** {
    *** Companion;
}
-keepclasseswithmembers class com.mach.apps.imccalculatorapp.** {
    kotlinx.serialization.KSerializer serializer(...);
}
