package org.roaringbitmap;

import java.util.BitSet;
import java.util.stream.IntStream;

/**
 * A {@link BitSet} implementation based on {@link RoaringBitmap}.
 */
public class RoaringBitSet extends BitSet {
  private static final long serialVersionUID = 1L;

  private final RoaringBitmap roaringBitmap;

  public RoaringBitSet() {
    super(0);
    roaringBitmap = new RoaringBitmap();
  }

  private RoaringBitSet(RoaringBitmap roaringBitmap) {
    super(0);
    this.roaringBitmap = roaringBitmap;
  }

  @Override
  public void set(int bitIndex) {
    roaringBitmap.add(bitIndex);
  }

  @Override
  public void set(int bitIndex, boolean value) {
    if (value) {
      roaringBitmap.add(bitIndex);
    } else {
      roaringBitmap.remove(bitIndex);
    }
  }

  @Override
  public void set(int fromIndex, int toIndex) {
    roaringBitmap.add((long) fromIndex, (long) toIndex);
  }

  @Override
  public void set(int fromIndex, int toIndex, boolean value) {
    if (value) {
      roaringBitmap.add((long) fromIndex, (long) toIndex);
    } else {
      roaringBitmap.remove((long) fromIndex, (long) toIndex);
    }
  }

  @Override
  public void clear(int bitIndex) {
    roaringBitmap.remove(bitIndex);
  }

  @Override
  public void clear(int fromIndex, int toIndex) {
    roaringBitmap.remove((long) fromIndex, (long) toIndex);
  }

  @Override
  public void clear() {
    roaringBitmap.clear();
  }

  @Override
  public boolean get(int bitIndex) {
    return roaringBitmap.contains(bitIndex);
  }

  @Override
  public BitSet get(int fromIndex, int toIndex) {
    RoaringBitmap newBitmap = roaringBitmap.selectRange(fromIndex, toIndex);
    // shift the bits to start from index 0
    newBitmap = RoaringBitmap.addOffset(newBitmap, -fromIndex);
    return new RoaringBitSet(newBitmap);
  }

  @Override
  public int nextSetBit(int fromIndex) {
    return (int) roaringBitmap.nextValue(fromIndex);
  }

  @Override
  public int nextClearBit(int fromIndex) {
    return (int) roaringBitmap.nextAbsentValue(fromIndex);
  }

  @Override
  public int previousSetBit(int fromIndex) {
    return (int) roaringBitmap.previousValue(fromIndex);
  }

  @Override
  public int previousClearBit(int fromIndex) {
    return (int) roaringBitmap.previousAbsentValue(fromIndex);
  }

  @Override
  public int length() {
    if (roaringBitmap.isEmpty()) {
      return 0;
    }
    return roaringBitmap.last() + 1;
  }

  @Override
  public boolean isEmpty() {
    return roaringBitmap.isEmpty();
  }

  @Override
  public boolean intersects(BitSet set) {
    if (set instanceof RoaringBitSet) {
      return RoaringBitmap.intersects(roaringBitmap, ((RoaringBitSet) set).roaringBitmap);
    }
    return RoaringBitmap.intersects(roaringBitmap, fromBitSet(set));
  }

  @Override
  public int cardinality() {
    return roaringBitmap.getCardinality();
  }

  @Override
  public void and(BitSet set) {
    if (set instanceof RoaringBitSet) {
      roaringBitmap.and(((RoaringBitSet) set).roaringBitmap);
    } else {
      roaringBitmap.and(fromBitSet(set));
    }
  }

  @Override
  public void or(BitSet set) {
    if (set instanceof RoaringBitSet) {
      roaringBitmap.or(((RoaringBitSet) set).roaringBitmap);
    } else {
      roaringBitmap.or(fromBitSet(set));
    }
  }

  @Override
  public void xor(BitSet set) {
    if (set instanceof RoaringBitSet) {
      roaringBitmap.xor(((RoaringBitSet) set).roaringBitmap);
    } else {
      roaringBitmap.xor(fromBitSet(set));
    }
  }

  @Override
  public void andNot(BitSet set) {
    if (set instanceof RoaringBitSet) {
      roaringBitmap.andNot(((RoaringBitSet) set).roaringBitmap);
    } else {
      roaringBitmap.andNot(fromBitSet(set));
    }
  }

  @Override
  public int hashCode() {
    return roaringBitmap.hashCode();
  }

  @Override
  public int size() {
    if (roaringBitmap.isEmpty()) {
      return 0;
    }
    int lastBit = Math.max(length(), 64);
    int remainder = lastBit % 64;
    return remainder == 0 ? lastBit : lastBit + 64 - remainder;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof RoaringBitSet) {
      return roaringBitmap.equals(((RoaringBitSet) obj).roaringBitmap);
    }
    return false;
  }

  @Override
  public Object clone() {
    return new RoaringBitSet(roaringBitmap.clone());
  }

  @Override
  public IntStream stream() {
    return roaringBitmap.stream();
  }

  @Override
  public String toString() {
    return roaringBitmap.toString();
  }

  @Override
  public void flip(int bitIndex) {
    roaringBitmap.flip((long) bitIndex, (long) bitIndex + 1);
  }

  @Override
  public void flip(int fromIndex, int toIndex) {
    roaringBitmap.flip((long) fromIndex, (long) toIndex);
  }

  @Override
  public long[] toLongArray() {
    return BitSetUtil.toLongArray(roaringBitmap);
  }

  @Override
  public byte[] toByteArray() {
    return BitSetUtil.toByteArray(roaringBitmap);
  }

  private static RoaringBitmap fromBitSet(BitSet bitSet) {
    return BitSetUtil.bitmapOf(bitSet);
  }
}
