package com.example.socialnetwork.repo;

import com.example.socialnetwork.exceptions.RepositoryException;

import java.sql.SQLException;
import java.util.List;

public interface Repository<E,ID> {
    void add(E entity)throws RepositoryException;
    void remove(E entity)throws RepositoryException;
    E find(ID id)throws RepositoryException;

    List<E> get_all() throws SQLException, RepositoryException;

}
