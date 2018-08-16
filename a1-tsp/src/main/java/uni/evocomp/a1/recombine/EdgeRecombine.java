package uni.evocomp.a1.recombine;

import java.util.ArrayList;
import java.util.List;
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
    return null;
  }

  private class TableEntry {
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
        edges.add(new Pair<>(node, false));
      }
    }

    public List<Pair<Integer, Boolean>> edges;
  }

  public class Table {
    public Table(int n) {
      entries = new ArrayList<>();
    }

    public void addEdge(int firstNode, int secondNode) {
      entries.get(firstNode).addEdge(secondNode);
      entries.get(secondNode).addEdge(firstNode);
    }

    public List<TableEntry> entries;
  }

  /**
   * Construct an edge table from two individuals
   * @param first   The first individual
   * @param second  The second individual
   * @return        An edge table showing
   */
  public Table constructTable(Individual first, Individual second) {
    int n = first.getGenotype().size();
    Table table = new Table(n);

    for (int i = 0; i < n; i += 1) {
      table.addEdge(first.getGenotype().get(i), first.getGenotype().get((i + 1) % n));
      table.addEdge(second.getGenotype().get(i), second.getGenotype().get((i + 1) % n));
    }

    return null;
  }
}
