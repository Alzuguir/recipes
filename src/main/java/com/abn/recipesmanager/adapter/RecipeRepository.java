package com.abn.recipesmanager.adapter;

import com.abn.recipesmanager.domain.domainobject.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends MongoRepository<Recipe, String>, QueryByExampleExecutor<Recipe> {
    @Query(value =
            "{$and:[" +
                    "{$or: [{?0: {$type: 'null'}}, {'ingredients.name': {$nin: ?0}}]}," +
                    "{$or: [{'?1': {$type: 'null'}}, {'name': {$regex: '?1'}}]}," +
                    "{$or: [{?2: {$type: 'null'}}, {'servings': {$eq: ?2}}]}," +
                    "{$or: [{?3: {$type: 'null'}}, {'vegetarian': ?3}]}," +
                    "{$or: [{?4: {$type: 'null'}}, {'ingredients.name': {$in:?4}}]}," +
                    "{$or: [{'?5': {$type: 'null'}}, {'instructions': {$regex: '?5'}}]}]}")
    List<Recipe> findByIngredientNotPresent(
            String[] ninIngredients,
            String name,
            Integer servings,
            Boolean vegetarian,
            String[] ingredients,
            String instructions
    );
}
