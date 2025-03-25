# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontwarn java.lang.invoke.StringConcatFactory

# Jangan hapus class Resource dan subclass-nya (Success, Error, Loading)
-keep class com.ammar.core.data.Resource { *; }
-keep class com.ammar.core.data.Resource$* { *; }

# Jangan hapus class pada domain layer
-keep class com.ammar.core.domain.model.Events { *; }
-keep class com.ammar.core.domain.repository.IEventsRepository { *; }
-keep class com.ammar.core.domain.usecase.EventsInteractor { *; }
-keep class com.ammar.core.domain.usecase.EventsUseCase { *; }

# Jangan hapus class di DI (Dependency Injection) Koin
-keep class com.ammar.core.di.CoreModuleKt { *; }

# Jangan hapus class adapter
-keep class com.ammar.core.ui.EventsAdapter { *; }
-keep class com.ammar.core.ui.EventsHorizAdapter { *; }

# Jika menggunakan Koin, tambahkan ini agar tidak dihapus oleh R8
-keep class org.koin.** { *; }
-keep class * extends org.koin.core.module.Module { *; }


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

-keepclassmembers class com.ammar.core.data.source.local.entity.EventsEntity {
    public java.lang.String toString();
}

-keep class java.lang.invoke.** { *; }

-dontwarn java.lang.invoke.StringConcatFactory
