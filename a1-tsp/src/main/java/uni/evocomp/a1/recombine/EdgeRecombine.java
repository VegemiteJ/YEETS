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
  public EdgeRecombine() { }

  @Override
  public Pair<Individual, Individual> recombineDouble(Individual firstParent, Individual secondParent) {
    return new Pair<>(recombine(firstParent, secondParent), recombine(firstParent, secondParent));
  }

  @Override
  public Individual recombine(Individual firstParent, Individual secondParent) {
    return recombine(firstParent, secondParent, new Random());
  }

  public Individual recombine(
      Individual firstParent,
      Individual secondParent,
      Random random) {
    Table table = constructTable(firstParent, secondParent);
    Integer n = firstParent.getGenotype().size();

    // First pick an initial element at random
    List<Integer> offspring = new ArrayList<>();
    offspring.add(random.nextInt(n));

    while (offspring.size() < n) {
      Integer current = offspring.get(offspring.size() - 1);
      table.removeEdgesTo(current);

      List<Integer> candidatesWithCommonEdge = new ArrayList<>();
      List<Integer> candidatesWithShortestList = new ArrayList<>();
      Integer currentShortestListSize = Integer.MAX_VALUE;

      // Build lists of common-edge nodes, then shortest-list nodes.
      for (Pair<Integer, Boolean> node : table.entry(current).edges) {
        if (node.second) {
          candidatesWithCommonEdge.add(node.first);
        } else if (table.entry(node.first).size() == currentShortestListSize) {
          candidatesWithShortestList.add(node.first);
        } else if (table.entry(node.first).size() < currentShortestListSize) {
          candidatesWithShortestList = new ArrayList<>();
          candidatesWithShortestList.add(node.first);
          currentShortestListSize = table.entry(node.first).size();
        }
      }

      // Select the next node.
      Integer selected;
      if (candidatesWithCommonEdge.size() > 0) {
        selected = candidatesWithCommonEdge.get(random.nextInt(candidatesWithCommonEdge.size()));
      } else if (candidatesWithShortestList.size() > 0) {
        selected = candidatesWithShortestList.get(random.nextInt(candidatesWithShortestList.size()));
      } else {
        selected = random.nextInt(n - offspring.size());
        while (offspring.contains(selected)) {
          selected += 1;
        }
      }
      assert !offspring.contains(selected);

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
