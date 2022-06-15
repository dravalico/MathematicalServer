package it.units.advancedprogramming.project.parsing;

import java.util.List;
import java.util.Objects;

public abstract class AbstractNode {
    private final List<AbstractNode> children;

    public AbstractNode(List<AbstractNode> children) {
        this.children = children;
    }

    public List<AbstractNode> getChildren() {
        return children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractNode abstractNode = (AbstractNode) o;
        return Objects.equals(children, abstractNode.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(children);
    }

}
