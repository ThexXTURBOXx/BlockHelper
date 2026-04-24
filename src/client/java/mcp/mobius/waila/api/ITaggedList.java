package mcp.mobius.waila.api;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ITaggedList<E, T> extends List<E> {

    boolean add(E e, T tag);

    boolean add(E e, Collection<? extends T> taglst);

    void add(int index, E e, T tag);

    void add(int index, E e, Collection<? extends T> taglst);

    boolean addAll(Collection<? extends E> c, T tag);

    boolean addAll(Collection<? extends E> c, Collection<? extends T> taglst);

    Set<T> getTags(E e);

    Set<T> getTags(int index);

    void addTag(E e, T tag);

    void addTag(int index, T tag);

    void removeTag(E e, T tag);

    void removeTag(int index, T tag);

    Set<E> getEntries(T tag);

    void removeEntries(T tag);

    String getTagsAsString(E e);

    boolean containsTag(T tag);

    int indexOfTag(T tag);

    int lastIndexOfTag(T tag);

    E replaceFirstTagEntry(E newEntry, T tag);

    E replaceLastTagEntry(E newEntry, T tag);

}
