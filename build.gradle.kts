import net.labymod.labygradle.common.extension.model.labymod.ReleaseChannel

plugins {
    id("net.labymod.labygradle")
    id("net.labymod.labygradle.addon")
}

val versions = providers.gradleProperty("net.labymod.minecraft-versions").get().split(";")

group = "org.example"
version = System.getenv().getOrDefault("VERSION", "1.0.0")

labyMod {
    defaultPackageName = "de.einsjustin.handtransformer" // change this to your main package name (used by all modules)

    minecraft {
        registerVersion(versions.toTypedArray()) {
            runs {
                getByName("client") {
                    // When the property is set to true, you can log in with a Minecraft account
                    // devLogin = true
                }
            }
        }
    }

    addonInfo {
        namespace = "hand_transformer"
        displayName = "Hand Transformer"
        author = "EinsJustin"
        description = "You can transform your hand and change the size of the item in your hand"
        minecraftVersion = "1.8<1.21.8"
        version = rootProject.version.toString()
    }
}

subprojects {
    plugins.apply("net.labymod.labygradle")
    plugins.apply("net.labymod.labygradle.addon")

    group = rootProject.group
    version = rootProject.version
}
