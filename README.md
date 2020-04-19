# pluto
A lightweight inventory menu and gui library for spigot plugins, providing extensive functionality

## What is pluto?
Pluto is lightweight inventory gui and menu library for spigot plugins.

Pluto aims to remove all the hassle that comes with making and handling menus/guis, providing a simple to use library that eliminates
this while also providing extensive functionality and features.

## Installing
 1. Clone the repository using (preferably using ssh): `git clone git@github.com:sagan1/pluto.git`
 2. Enter the directory with `cd pluto`, then install pluto with maven using `mvn clean package install`
 3. Add pluto to your project's `pom.xml`'s dependencies:
```xml
<dependency>
    <groupId>com.sagan</groupId>
    <artifactId>pluto</artifactId>
    <version>1.0.0</version> <!-- at the time of writing, current version is 1.0.0. Check the pom for the latest version -->
    <scope>provided</scope>
</dependency>
````

## Using pluto
Pluto is simple. It's based off a polymorphic structure in which menus extend from `Menu` and the buttons/items with in are simply instances of `MenuItem`

### Menu
All menus managed and run by pluto must **extend** from `Menu`. Simply override the default constructor in `Menu` and pass in the values to
to `Menu`, keeping `Player` as a constructor parameter as menus in pluto are player specific
```java
import com.sagan.pluto.menu.Menu;
import org.bukkit.entity.Player;

public class ExampleMenu extends Menu {
    
    // Makes a new menu with 6 rows (6 * 9 = 54 slots), title "My Test Menu"
    public ExampleMenu(Player player) {
        super(6, "My Test Menu", player);
    }
}
```
Open the menu for the player using:
```java
// Menu#open();
new ExampleMenu(player).open();
```
Don't forget to call `Menu#open();`
<br>
Subsequently you can call `Menu#close();` to close the menu for the player if needbe.

#### Adding items/buttons/interactables
Adding items to a menu is simple, it can be done dynamically at runtime or by default. Take the previous example menu.
```java
import com.sagan.pluto.menu.Menu;
import org.bukkit.entity.Player;

public class ExampleMenu extends Menu {
    
    public ExampleMenu(Player player) {
        super(6, "My Test Menu", player);
        
        // An item with no function
        super.addMenuItem(new MenuItem(new ItemStack(Material.APPLE), 5));

        // An item with a function
        super.addMenuItem(new MenuItem(new ItemStack(Material.COMPASS), 1, viewer -> viewer.sendMessage("hello!")));
    }
}
```
`Plain Item` is a simple item that sits in a slot with no function. This is an item that might be something you wish players to be able to take
 - Add one using `super.addPlainItem(...)`
 
`MenuItem` takes a minimum of **2** constructor parameters:
 - `ItemStack item` the item that actually represents this item/button/interactable in the menu
 - `int slot` the slot this item will appear in
 - **(Optional)** `Consumer<Player> onClickFunction` the function that will run when the item is interacted with successfully

***Important!***
 - If the slot of the menu item you want to add is already taken by an item, it will **not** be overridden. If you want to override it, use `super.setItem(...)`

#### Slot Flags
Pluto provides functionality for allowing certain slots to be added to or takne from; this is done via `SlotFlag`
<br>
**NOTE:** By defualt, all slots are neither allowed to be taken from, nor palced into.
<br>
Example:
```java
import com.sagan.pluto.menu.Menu;
import org.bukkit.entity.Player;

public class ExampleMenu extends Menu {
    
    public ExampleMenu(Player player) {
        super(6, "My Test Menu", player);
        
        // Adding slot flags
        super.addSlotFlag(4, SlotFlag.ALLOW_PLACE_INTO, SlotFlag.ALLOW_TAKE_FROM);
        super.addSlotFlag(7, SlotFlag.ALLOW_TAKE_FROM);
        
        super.removeAllSlotFlags(4); // Removes ALL slot flags from the slot
        super.setSlotFlags(7, SlotFlag.ALLOW_PLACE_INTO); // Overrides all slot flags, setting them to the new one(s)
    }
}
```

#### Menu Fill
You can fill ***empty*** slots in menus with background/palceholder using a variety of methods:
```java
import com.sagan.pluto.menu.Menu;
import org.bukkit.entity.Player;

public class ExampleMenu extends Menu {

    public ExampleMenu(Player player) {
        super(6, "My Test Menu", player);
        
        // Fills all non-taken slots
        super.fillAll();
        
        // Fills all non-taken slots except for the provided slots
        super.fillAllExcept(1, 6, 9, 34, 28);

        // Fills only these slots 
        super.fillOnly(7, 8, 9, 52, 41);
    }
}
```
These methods can be called numerous times to create cool patterns.
<br>
**ProTip:** You can change the item the menu uses to fill by calling `Menu#setFillItem(ItemStack item);`
