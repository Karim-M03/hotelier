package shared;

public enum Badge {
    RECENSORE("Recensore"),
    RECENSORE_ESPERTO("Recensore Esperto"),
    CONTRIBUTORE("Contributore"),
    CONTRIBUTORE_ESPERTO("Contributore Esperto"),
    CONTRIBUTORE_SUPER("Contributore Super");

    private final String name;

    Badge(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
