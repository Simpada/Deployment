package no.kristiania.deployment;

public enum ProductCategory {
    A_FORK,
    FORKSES,
    ALSO_FORKS,
    FORKS;

    public static ProductCategory parse(String s) {
        s = s.toUpperCase();
        return switch (s) {
            case "A_FORK" -> A_FORK;
            case "FORKSES" -> FORKSES;
            case "ALSO_FORKS" -> ALSO_FORKS;
            case "FORKS" -> FORKS;
            default -> null;
        };
    }

    @Override
    public String toString() {
        String name = this.name();
        name = name.replace('_', ' ');
        name = name.toLowerCase();

        return name;
    }
}
