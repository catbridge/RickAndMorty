package com.example.rickandmorty.data.repository

import com.example.rickandmorty.data.database.CharacterDao
import com.example.rickandmorty.data.mapper.toCharacterEntity
import com.example.rickandmorty.data.mapper.toDomainModel
import com.example.rickandmorty.data.model.ExtraRickDTO
import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.repository.CharacterLocalRepository

internal class CharacterLocalRepositoryImpl(private val characterDao: CharacterDao) :
    CharacterLocalRepository {


    override suspend fun getCharacters(limit: Int, offset: Int): List<Character> {
        return characterDao.getCharacters(limit, offset).map { it.toDomainModel() }
    }

    override suspend fun getAllCharacters(): List<Character> {
        return characterDao.getAllCharacters().map { it.toDomainModel() }
    }

    override suspend fun insert(character: Character) {
        characterDao.insert(character.toCharacterEntity())
    }

    override suspend fun insertList(characters: List<Character>) {
        characterDao.insertList(characters.map { it.toCharacterEntity() })
    }


    override suspend fun delete(character: Character) {
        characterDao.delete(character.toCharacterEntity())
    }

    override suspend fun createExtraItem(): Character {
        return ExtraRickDTO().toDomainModel()
    }
}