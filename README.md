[![Licence](https://img.shields.io/github/license/Ileriayo/markdown-badges?style=for-the-badge)](./LICENSE)
![Kotlin](https://img.shields.io/badge/kotlin-%230095D5.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)

## Roomie
Roomie is an annotation processing library that utilizes KSP to generate TypeConverter classes for Room. TypeConverter classes most often involve same boiler-plate code and Roomie makes it really easy to quickly create them with a single annotation.

## Usage
* #### Annotate the model class you want to generate a TypeConverter for using: ```@AddConverter```
    ```
    @AddConverter
    data class Person(val firstName: String, val lastName: String)
    ```

    It's as simple as that! 
    Rebuild the project to generate the Converter. A converter class with the name ```PersonConverter``` will be generated for the ```Person``` data class     above.
    In your case use [Model name]Converter to get the corresponding converter for your data class.

* #### Specifying converter name
    By default, the generated converter has **Converter** appended to its name but it can be changed by using the ```name``` argument specified in             ```@AddConverter``` annotation.
    ```
    @AddConverter(name = "PeopleConverter")
    data class Person(val firstName: String, val lastName: String)
    ```
    This will will generate the converter using ```PeopleConverter``` as its name.

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

