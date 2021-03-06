package org.apache.hop.metadata.serializer.json.person;

import org.apache.hop.metadata.api.HopMetadata;
import org.apache.hop.metadata.api.HopMetadataProperty;
import org.apache.hop.metadata.api.IHopMetadata;
import org.apache.hop.metadata.serializer.json.occupation.Occupation;
import org.apache.hop.metadata.serializer.json.person.interest.IInterest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@HopMetadata(
  key="person",
  name="A Person",
  description = "Description of the Person object"
)
public class Person implements IHopMetadata {

  @HopMetadataProperty
  private String name;

  @HopMetadataProperty
  private String age;

  @HopMetadataProperty
  private Address address;

  @HopMetadataProperty
  private IInterest mainInterest;

  @HopMetadataProperty
  private IInterest sideInterest;

  @HopMetadataProperty
  private List<IInterest> interests;

  @HopMetadataProperty
  private Map<String,String> attributes;

  @HopMetadataProperty( storeWithName = true)
  private Occupation occupation;

  public Person() {
    interests = new ArrayList<>();
    attributes = new HashMap<>();
  }

  public Person( String name, String age, Address address, IInterest mainInterest, List<IInterest> interests, Map<String, String> attributes, Occupation occupation ) {
    this.name = name;
    this.age = age;
    this.address = address;
    this.mainInterest = mainInterest;
    this.interests = interests;
    this.attributes = attributes;
    this.occupation = occupation;
  }

  @Override public boolean equals( Object o ) {
    if ( this == o ) {
      return true;
    }
    if ( o == null || getClass() != o.getClass() ) {
      return false;
    }
    Person person = (Person) o;
    return Objects.equals( name, person.name ) &&
      Objects.equals( age, person.age ) &&
      Objects.equals( address, person.address ) &&
      Objects.equals( mainInterest, person.mainInterest ) &&
      Objects.equals( sideInterest, person.sideInterest ) &&
      Objects.equals( interests, person.interests ) &&
      Objects.equals( attributes, person.attributes ) &&
      Objects.equals( occupation, person.occupation );
  }

  @Override public int hashCode() {
    return Objects.hash( name, age, address, mainInterest, sideInterest, interests, attributes, occupation );
  }

  /**
   * Gets name
   *
   * @return value of name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name The name to set
   */
  public void setName( String name ) {
    this.name = name;
  }

  /**
   * Gets age
   *
   * @return value of age
   */
  public String getAge() {
    return age;
  }

  /**
   * @param age The age to set
   */
  public void setAge( String age ) {
    this.age = age;
  }

  /**
   * Gets address
   *
   * @return value of address
   */
  public Address getAddress() {
    return address;
  }

  /**
   * @param address The address to set
   */
  public void setAddress( Address address ) {
    this.address = address;
  }

  /**
   * Gets mainInterest
   *
   * @return value of mainInterest
   */
  public IInterest getMainInterest() {
    return mainInterest;
  }

  /**
   * @param mainInterest The mainInterest to set
   */
  public void setMainInterest( IInterest mainInterest ) {
    this.mainInterest = mainInterest;
  }

  /**
   * Gets sideInterest
   *
   * @return value of sideInterest
   */
  public IInterest getSideInterest() {
    return sideInterest;
  }

  /**
   * @param sideInterest The sideInterest to set
   */
  public void setSideInterest( IInterest sideInterest ) {
    this.sideInterest = sideInterest;
  }

  /**
   * Gets interests
   *
   * @return value of interests
   */
  public List<IInterest> getInterests() {
    return interests;
  }

  /**
   * @param interests The interests to set
   */
  public void setInterests( List<IInterest> interests ) {
    this.interests = interests;
  }

  /**
   * Gets attributes
   *
   * @return value of attributes
   */
  public Map<String, String> getAttributes() {
    return attributes;
  }

  /**
   * @param attributes The attributes to set
   */
  public void setAttributes( Map<String, String> attributes ) {
    this.attributes = attributes;
  }

  /**
   * Gets occupation
   *
   * @return value of occupation
   */
  public Occupation getOccupation() {
    return occupation;
  }

  /**
   * @param occupation The occupation to set
   */
  public void setOccupation( Occupation occupation ) {
    this.occupation = occupation;
  }
}
