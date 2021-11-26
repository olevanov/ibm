package ibm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;


/**
 * Super Iterator
 * 
 * @author Anton Aliavanau
 *
 */
class SuperIterator implements Iterator<Integer> {

  private PriorityQueue<Item> heap = new PriorityQueue<>();
  private Map<Integer, Iterator<Integer>> map = new HashMap<>();

  public SuperIterator(Collection<Iterator<Integer>> iterators) {
    int index = 0;
    for (Iterator<Integer> iter : iterators) {
      index++;
      if (iter.hasNext()) {
        heap.add(new Item(iter.next(), index));
        if (iter.hasNext())
          map.put(index, iter);
      }
    }
  }

  @Override
  public Integer next() {
    Item item = heap.poll();
    Iterator<Integer> iterator = map.get(item.index);
    if (iterator != null && iterator.hasNext()) {
      heap.add(new Item(iterator.next(), item.index));
    }
    return item.value;
  }

  @Override
  public boolean hasNext() {
    return !heap.isEmpty();
  }

  class Item implements Comparable<Item> {
    int value;
    int index;

    Item(int value, Integer index) {
      this.value = value;
      this.index = index;
    }

    @Override
    public boolean equals(Object obj) {
      return value == ((Item) obj).value;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + getOuterType().hashCode();
      result = prime * result + index;
      result = prime * result + value;
      return result;
    }

    @Override
    public int compareTo(SuperIterator.Item o) {
      return value - o.value;
    }

    private SuperIterator getOuterType() {
      return SuperIterator.this;
    }
  }

}
