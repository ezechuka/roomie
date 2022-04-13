[![Licence](https://img.shields.io/github/license/Ileriayo/markdown-badges?style=for-the-badge)](./LICENSE)
![Kotlin](https://img.shields.io/badge/kotlin-%230095D5.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)

## Roomie
Roomie is an annotation processing library that utilizes KSP to generate TypeConverter classes for Room. TypeConverter classes most often involve same boiler-plate code and Roomie makes it really easy to quickly create them with a single annotation.

## Usage


## Download
[![JitPack](https://img.shields.io/jitpack/v/github/ezechuka/roomie?color=%2346C018&style=for-the-badge)](https://jitpack.io/#ezechuka/roomie/)

### 1. Add the KSP plugin:
    
> The version you chose for the KSP plugin depends on the Kotlin version your project uses. <br>
You can check https://github.com/google/ksp/releases for the list of KSP versions, then pick the last release that matches your Kotlin version.  
Example: If you're using 1.5.30 Kotlin version, then the corresponding KSP version is 1.5.30-1.0.0.

**groovy - build.gradle (module level)**
```gradle
plugins {
    //...
    id 'com.google.devtools.ksp' version '1.5.30-1.0.0' // Depends on your kotlin version
}
```

**kotlin - build.gradle.kts (module level)**
```gradle
plugins {
    //...
    id("com.google.devtools.ksp") version "1.5.30-1.0.0" // Depends on your kotlin version
}
```
    
### 2. Add depedencies:

**groovy - build.gradle (module level)**
```gradle
implementation 'com.github.ezechuka.roomie:roomie-annotation:1.0.0-beta02'
ksp 'com.github.ezechuka.roomie:roomie-processor:1.0.0-beta02'
```

**kotlin - build.gradle.kts (module level)**
```gradle
implementation("com.github.ezechuka.roomie:roomie-annotation:1.0.0-beta02")
ksp("com.github.ezechuka.roomie:roomie-processor:1.0.0-beta02")
```

### 3. Specify build generated folder to IDE
Within the ```android``` block add:

**groovy - build.gradle (module level)**
```gradle
applicationVariants.all { variant ->
    kotlin.sourceSets {
        getByName(variant.name) {
            kotlin.srcDir("build/generated/ksp/${variant.name}/kotlin")
        }
    }
}
```

**kotlin - build.gradle.kts (module level)**
```gradle
applicationVariants.all {
    kotlin.sourceSets {
        getByName(name) {
            kotlin.srcDir("build/generated/ksp/$name/kotlin")
        }
    }
}
```

