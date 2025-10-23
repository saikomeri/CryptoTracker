# Retrofit
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.sai.cryptotracker.data.remote.dto.** { *; }

# Moshi
-keep class com.squareup.moshi.** { *; }
-keepclassmembers class ** { @com.squareup.moshi.* <methods>; }

# Room
-keep class com.sai.cryptotracker.data.local.entity.** { *; }

# Hilt
-keep class dagger.hilt.** { *; }
