package mcp.mobius.waila.api;

public interface Replacer<E> {

    E replace(E s);

    class Prepender implements Replacer<String> {

        private final String prefix;

        public Prepender(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public String replace(String s) {
            return prefix + s;
        }

    }

    class Appender implements Replacer<String> {

        private final String suffix;

        public Appender(String suffix) {
            this.suffix = suffix;
        }

        @Override
        public String replace(String s) {
            return s + suffix;
        }

    }

    class DefaultReplacer implements Replacer<String> {

        private final String from;
        private final String to;

        public DefaultReplacer(String from, String to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public String replace(String s) {
            return s.replace(from, to);
        }

    }

    class RegexReplacer implements Replacer<String> {

        private final String pattern;
        private final String to;

        public RegexReplacer(String pattern, String to) {
            this.pattern = pattern;
            this.to = to;
        }

        @Override
        public String replace(String s) {
            return s.replaceAll(pattern, to);
        }

    }

}
