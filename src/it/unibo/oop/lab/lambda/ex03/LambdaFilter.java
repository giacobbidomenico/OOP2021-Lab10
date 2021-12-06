package it.unibo.oop.lab.lambda.ex03;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Modify this small program adding new filters.
 * Realize this exercise using as much as possible the Stream library.
 * 
 * 1) Convert to lowercase
 * 
 * 2) Count the number of chars
 * 
 * 3) Count the number of lines
 * 
 * 4) List all the words in alphabetical order
 * 
 * 5) Write the count for each word, e.g. "word word pippo" should output "pippo -> 1 word -> 2"
 *
 */
public final class LambdaFilter extends JFrame {

    private static final long serialVersionUID = 1760990730218643730L;
    /**
     * Enumeration that implements the filters.
     */
    private enum Command {
        IDENTITY("No modifications", Function.identity()),
        TO_LOWER_CASE("To lower case", String :: toLowerCase),
        NUMBER_OF_CHARS("Count the number of chars", counts -> Integer.toString(counts.length())),
        NUMBER_OF_LINES("Count the number of lines", counts -> Integer.toString(counts.split("\r\n|\r|\n").length)),
        SORTED_WORDS("List all the words in alphabetical order", Command::alphabeticOrder),
        COUNT_WORDS("Write the count for each word", Command::countWords);

        private final String commandName;
        private final Function<String, String> fun;
        /**
         * Builds a new {@link Command}.
         * 
         * @param name
         * @param process
         */
        Command(final String name, final Function<String, String> process) {
            commandName = name;
            fun = process;
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return commandName;
        }
        /**
         * Method that applies filters.
         * 
         * @param s
         * @return a string to which filters have been applied
         */
        public String translate(final String s) {
            return fun.apply(s);
        }
        /**
         * Method that sort (in alphabetical order) the words present in a string.
         * 
         * @param string
         * @return a string with the words sorted alphabetically 
         */
        private static String alphabeticOrder(final String string) {
            return List.of(string.split("\\s+")).stream()
                    .sorted()
                    .collect(Collectors.joining("\n"));
        }
        /**
         * Method that counts how many times the words are present in a string.
         * 
         * @param string
         * @return a string in which each word is associated with the number of times it appears 
         *         in the string passed as a parameter
         */
        private static String countWords(final String string) {
            return List.of(string.split("\\s+")).stream()
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                    .entrySet().stream()
                    .map(tmp -> tmp.getKey() + " -> " + tmp.getValue())
                    .collect(Collectors.joining("\n"));
        }
    }
    /**
     * Builds a new {@link LambdaFilter}.
     */
    private LambdaFilter() {
        super("Lambda filter GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final JPanel panel1 = new JPanel();
        final LayoutManager layout = new BorderLayout();
        panel1.setLayout(layout);
        final JComboBox<Command> combo = new JComboBox<>(Command.values());
        panel1.add(combo, BorderLayout.NORTH);
        final JPanel centralPanel = new JPanel(new GridLayout(1, 2));
        final JTextArea left = new JTextArea();
        left.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        final JTextArea right = new JTextArea();
        right.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        right.setEditable(false);
        centralPanel.add(left);
        centralPanel.add(right);
        panel1.add(centralPanel, BorderLayout.CENTER);
        final JButton apply = new JButton("Apply");
        apply.addActionListener(ev -> right.setText(((Command) combo.getSelectedItem()).translate(left.getText())));
        panel1.add(apply, BorderLayout.SOUTH);
        setContentPane(panel1);
        final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        final int sw = (int) screen.getWidth();
        final int sh = (int) screen.getHeight();
        setSize(sw / 4, sh / 4);
        setLocationByPlatform(true);
    }
    /**
     * @param a unused
     */
    public static void main(final String... a) {
        final LambdaFilter gui = new LambdaFilter();
        gui.setVisible(true);
    }
}
