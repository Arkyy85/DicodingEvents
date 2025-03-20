-keep,includedescriptorclasses class net.sqlcipher.** { *; }
-keep,includedescriptorclasses interface net.sqlcipher.** { *; }
-keepattributes Signature

-keepattributes *Annotation*

-dontwarn sun.misc.**

-keep class com.google.gson.examples.android.model.** { <fields>; }

-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

-keep class com.ammar.core.data.source.remote.response.** { *; }
-keep class com.google.gson.** { *; }
-keep class com.google.gson.internal.bind.**
-keep class com.google.gson.reflect.TypeToken
-keepattributes *Annotation*
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-keepclassmembers class * {
    @com.google.gson.annotations.Expose <fields>;
}


-keepclassmembers,allowobfuscation class * {
@com.google.gson.annotations.SerializedName <fields>;
}

-keepattributes Signature, InnerClasses, EnclosingMethod

-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

-keepclassmembers,allowshrinking,allowobfuscation interface * {
@retrofit2.http.* <methods>;
}

-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

-dontwarn javax.annotation.**

-dontwarn kotlin.Unit

-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

-dontwarn kotlinx.**

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
<init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
**[] $VALUES;
public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
*** rewind();
}

-keep class kotlin.LazyKt { *; }
-keep class kotlin.LazyKt__LazyJVMKt { *; }
-keep class kotlin.LazyKt__LazyKt { *; }
-keep class kotlin.jvm.functions.** { *; }
-keep class kotlin.Metadata { *; }
-keepclassmembers class kotlin.Metadata { *; }

-keep class androidx.viewbinding.** { *; }
-keep class androidx.databinding.** { *; }
-keep class android.databinding.** { *; }

# Mencegah penghapusan class terkait Binding
-keepclassmembers class * {
    @androidx.databinding.* <fields>;
}