package com.p4.p416.applications;

import java.util.*;

import com.p4.p416.gen.AbstractBaseExt;
import com.p4.utils.Utils.Pair;

import lombok.Data;

@Data
public class SsaForm {
	//constructor
	public SsaForm(){
		minMap = new HashMap<AbstractBaseExt,Integer>();
		assignCtx = null;
		parentCtx = null;
		allVer = new HashMap<>();
		rhsVer = new HashMap<>();
		phiVer = new HashMap<>();
	}

	// minMap is used to compute the minVersion before entering the if block.This is used
	// for phi computation.
	Map<AbstractBaseExt,Integer> minMap;

	// assignCtx is pass down the context of the closest assignment context.
	AbstractBaseExt assignCtx;

	// parentCtx is to pass down the closest ifstatement context.
	// Both are needed because if there is a child of ifstatement which is an assignment
	// statement . Then when we are in the lvalue context we need both the ifstatement
	// as well as the assignmentcontext to markDelete or to compute phi.
	AbstractBaseExt parentCtx;

	// allVer is allVersions of a variable ever computed in both rhs and lvalue context.
	// i.e. all definitions.
	Map<AbstractBaseExt,List<Pair<AbstractBaseExt,AbstractBaseExt> >> allVer;

	// rhsVer is versions of a variable in RHS context like expression contexts.
	// This determines whether can lvalue version from assignment is ever read or used.
	// If not it can be marked delete.
	Map<AbstractBaseExt,List<Pair<AbstractBaseExt,AbstractBaseExt> >> rhsVer;

	// Set of all phi versions of a variable. This is required so that a phi should be
	// marked non-deleted when it constitutes a new phi i.e. nested if's.
	// i.e outer phi is composed of inner phi.
	Map<AbstractBaseExt,Set<Integer> > phiVer ;


	public Set< Pair<AbstractBaseExt, Integer> > hashSetAllPhi = new HashSet<>();
	public Set< Pair<AbstractBaseExt, Integer> > hashSetRhsPhi = new HashSet<>();
	public void addPhiVer(Pair<AbstractBaseExt,Integer> x){
		hashSetAllPhi.add(x);
	}


}
