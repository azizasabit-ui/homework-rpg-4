package com.narxoz.rpg.composite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PartyComposite implements CombatNode {
    private final String name;
    private final List<CombatNode> children = new ArrayList<>();

    public PartyComposite(String name) {
        this.name = name;
    }

    public void add(CombatNode node) {
        children.add(node);
    }

    public void remove(CombatNode node) {
        children.remove(node);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getHealth() {
        int totalHealth = 0;
        for (CombatNode child : children) {
            totalHealth += child.getHealth();
        }
        return totalHealth;
    }

    @Override
    public int getAttackPower() {
        int totalAttack = 0;
        for (CombatNode child : getAliveChildren()) {
            totalAttack += child.getAttackPower();
        }
        return totalAttack;
    }

    @Override
    public void takeDamage(int amount) {
        List<CombatNode> aliveChildren = getAliveChildren();
        if (aliveChildren.isEmpty()) return;
        
        int damagePerChild = amount / aliveChildren.size();
        int remainder = amount % aliveChildren.size();
        
        for (int i = 0; i < aliveChildren.size(); i++) {
            CombatNode child = aliveChildren.get(i);
            int childDamage = damagePerChild + (i < remainder ? 1 : 0);
            child.takeDamage(childDamage);
        }
    }

    @Override
    public boolean isAlive() {
        return !getAliveChildren().isEmpty();
    }

    @Override
    public List<CombatNode> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public void printTree(String indent) {
        int aliveCount = getAliveChildren().size();
        System.out.println(indent + "+ " + name + " [Total HP=" + getHealth() + 
                          ", Total ATK=" + getAttackPower() + 
                          ", Alive: " + aliveCount + "/" + children.size() + "]");
        
        for (CombatNode child : children) {
            child.printTree(indent + "  ");
        }
    }

    private List<CombatNode> getAliveChildren() {
        return children.stream()
                      .filter(CombatNode::isAlive)
                      .collect(Collectors.toList());
    }
}