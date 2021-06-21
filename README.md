<p align="center">
    <img src="https://i.imgur.com/l1JdYMt.png" alt="Banner">
</p>

FroXty is JavaFX library which replicates the famous iOS translucent effect with ease.

![Demo](https://i.imgur.com/Ri1srhg.gif) 

## Set-up

FroXty can be imported into your project either by downloading the JAR file (see releases) or via Maven/Gradle through JitPack.

### Maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
<dependency>
    <groupId>com.github.iAmGio</groupId>
    <artifactId>froxty</artifactId>
    <version>1.3.0</version>
</dependency>
```

### Gradle
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
dependencies {
    implementation 'com.github.iAmGio:froxty:1.3.0'
}
```

## Getting started

The following piece of code will generate a frosty effect out of any node:
```java
//...
FrostyEffect effect = new FrostyEffect(opacity, updateTime); // Instantiates the effect. The parameters are optional and default to (0.5, 10)
FrostyBox box = new FrostyBox(effect, node); // Instantiates a container with frosty effect
box.setAntialiasingLevel(0.4); // See notes below
root.getChildren().add(box); // Adds the container to the scene
```

Then it's possible to style it:
```css
.frosty-box {
    -fx-background-radius: 20;
    -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, .5), 15, 0, 0, 5);
}

.frosty-box > * {
    -fx-background-color: rgba(255, 255, 255, .4);
    -fx-background-radius: inherit;
}
```

## Important notes
- Target nodes must not be directly added to the root.
- Make sure to set the anti-aliasing level via `FrostyBox#setAntialiasingLevel` when working with semi-transparent nodes. `0` will result in sharp curves, while `1` will result in non-opaque pixels getting excluded from the effect; it defaults to `0.35`. It is recommended that you set it to the minimum opacity value of the target node.
- Applying drop shadows to the target node results in visual errors. Apply effects to the Frosty Box instead.