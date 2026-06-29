# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Keep keyboard theme data classes (used with Gson reflection-based serialization)
-keep class com.colorkey.keyboard.themes.KeyboardTheme { *; }
-keep class com.colorkey.keyboard.effects.** { *; }
-keep class com.colorkey.keyboard.sound.** { *; }

# Gson specific rules
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
