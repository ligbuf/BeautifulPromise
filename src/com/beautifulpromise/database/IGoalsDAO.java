package com.beautifulpromise.database;

import java.util.ArrayList;

import com.beautifulpromise.common.dto.AddPromiseDTO;

public interface IGoalsDAO {

	public boolean insert(AddPromiseDTO addPromiseDTO);
	public AddPromiseDTO get(int id);
	public ArrayList<AddPromiseDTO> getList();
	AddPromiseDTO get(String postId);
}
