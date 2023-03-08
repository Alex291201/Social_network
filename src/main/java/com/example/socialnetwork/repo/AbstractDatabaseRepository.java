package com.example.socialnetwork.repo;

import com.example.socialnetwork.domain.AbstractHasID;
import com.example.socialnetwork.exceptions.RepositoryException;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractDatabaseRepository <E extends AbstractHasID<ID>,ID> implements Repository<E, ID>{
    protected final Connection con;

    protected AbstractDatabaseRepository(Connection con) {
        this.con = con;
    }

    @Override
    public void add(E entity) throws RepositoryException {
        try {
            insertEntity(entity);
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    protected abstract void insertEntity(E entity) throws SQLException;

    @Override
    public void remove(E entity) throws RepositoryException {
        try {
            deleteEntity(entity);
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    protected abstract void deleteEntity(E entity) throws SQLException;



    @Override
    public E find(ID id){
        try {
            return getEntity(id);
        } catch (SQLException | RepositoryException e) {
            return null;
        }
    }
    protected abstract E getEntity(ID id) throws SQLException, RepositoryException;
}
