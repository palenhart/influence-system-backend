package com.thecorporateer.influence.objects;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Zollak
 * 
 *         Entity used to store auctions
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Auction extends JpaEntity {

	/// Bild/

	@NotNull
	@NotBlank
	private String beginningTimestamp;
	@NotNull
	@NotBlank
	private String endingTimestamp;
	@NotNull
	@NotBlank
	private String title;
	@NotNull
	@NotBlank
	private String description;
	@ManyToOne
	private Corporateer highestBidder;
	@ManyToOne
	private Corporateer creator;
	@ManyToOne
	private Division usableInfluenceDivision;
	private Long highestBid;
	private Long currentBid;
	private Long minBid;
	private Long minStep;

}
