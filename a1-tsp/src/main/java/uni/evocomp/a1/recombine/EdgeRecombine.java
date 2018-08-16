package uni.evocomp.a1.recombine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import uni.evocomp.a1.Individual;
import uni.evocomp.util.IntegerPair;
import uni.evocomp.util.Pair;

public class EdgeRecombine implements Recombine {
  public EdgeRecombine() {
    ;
  }

  @Override
  public Pair<Individual, Individual> recombine(Individual firstParent, Individual secondParent) {
    return new Pair<>(recombineSingle(firstParent, secondParent), null);
  }

  public Individual recombineSingle(Individual firstParent, Individual secondParent) {
    Table table = constructTable(firstParent, secondParent);
    Integer n = firstParent.getGenotype().size();
    Random random = new Random();

    // First pick an initial element at random
    List<Integer> offspring = new ArrayList<>();
    offspring.add(random.nextInt(n));

    while (offspring.size() < n) {
      Integer current = offspring.get(offspring.size() - 1);

      // Select the next edge (current, other)
      // If there is a common edge between the two individuals, use that, otherwise pick
      // the entry which has the shortest list of possible edges.
      // (Ties are split at random).
      List<Integer> candidates = new ArrayList<>();
      for (Pair<Integer, Boolean> node : table.entry(current).edges) {
        if (node.second) candidates.add(node.first);
      }

      if (!candidates.isEmpty()) {
        // Select a node from candidates, add to the offspring, and update current.
        Integer selected = candidates.get(random.nextInt(candidates.size()));
        offspring.add(selected);
        table.removeEdgesTo(selected);
        continue;
      }

      // If there are no common edges from current, look for the element(s) with the shortest list
      // of possible edges.
      // Build list of possible next edges with the shortest list.
      int shortestListSize = Integer.MAX_VALUE;
      for (Pair<Integer, Boolean> node : table.entry(current).edges) {
        int currentSize = table.entry(node.first).size();
        if (currentSize < shortestListSize) {
          candidates = new ArrayList<>();
          shortestListSize = currentSize;
          candidates.add(node.first);
        } else if (currentSize == shortestListSize) {
          candidates.add(node.first);
        }
      }

      Integer selected = candidates.get(random.nextInt(candidates.size()));
      offspring.add(selected);
      table.removeEdgesTo(selected);
    }

    Individual result = new Individual(offspring);
    result.assertIsValidTour();
    return result;
  }

  public class TableEntry {
    public TableEntry() {
      edges = new ArrayList<>();
    }

    public int size() {
      return edges.size();
    }

    public void addEdge(Integer node) {
      for (int i = 0; i < size(); i += 1) {
        if (edges.get(i).first == node) {
          edges.get(i).second = true;
          return;
        }
      }
      edges.add(new Pair<>(node, false));
    }

    public String toString() {
      String s = "[ ";
      for (Pair<Integer, Boolean> edge : edges) {
        s += edge.toString() + " ";
      }
      return s + "]";
    }

    public List<Pair<Integer, Boolean>> edges;
  }

  public class Table {
    public Table(int n) {
      entries = new ArrayList<>();
      for (int i = 0; i < n; i += 1) {
        entries.add(new TableEntry());
      }
    }

    public void addEdge(int firstNode, int secondNode) {
      entry(firstNode).addEdge(secondNode);
      entry(secondNode).addEdge(firstNode);
    }

    public TableEntry entry(Integer i) {
      return entries.get(i - 1);
    }

    public void sortEntries() {
      for (TableEntry entry : entries) {
        Collections.sort(entry.edges, (a, b) -> a.first.compareTo(b.first));
      }
    }

    public void removeEdgesTo(Integer node) {
      for (TableEntry entry : entries) {
        entry.edges.removeIf((edge) -> edge.first.equals(node));
      }
    }

    public String toString() {
      String s = "";
      for (TableEntry entry : entries) {
        s += entry.toString() + "\n";
      }
      return s;
    }

    private List<TableEntry> entries;
  }

  /**
   * Construct an edge table from two individuals
   * @param first   The first individual
   * @param second  The second individual
   * @return        An edge table showing the edges in each tour and with common edges marked.
   */
  public Table constructTable(Individual first, Individual second) {
    int n = first.getGenotype().size();
    Table table = new Table(n);

    for (int i = 0; i < n; i += 1) {
      table.addEdge(first.getGenotype().get(i), first.getGenotype().get((i + 1) % n));
      table.addEdge(second.getGenotype().get(i), second.getGenotype().get((i + 1) % n));
    }

    table.sortEntries();
    return table;
  }
}
