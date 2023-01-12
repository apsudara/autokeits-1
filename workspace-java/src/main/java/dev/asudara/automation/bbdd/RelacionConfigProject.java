// package dev.asudara.automation.bbdd;

// import java.io.Serializable;

// import javax.persistence.Column;
// import javax.persistence.Embeddable;

// import lombok.EqualsAndHashCode;

// @EqualsAndHashCode 
// @Embeddable
// public class RelacionConfigProject implements  Serializable {
//     @Column(name = "project_id")
//     Long projectId;

//     @Column(name = "configs_id")
//     Long configId;

//     public RelacionConfigProject() {
//     }

//     public Long getProjectId() {
//         return projectId;
//     }

//     public void setProjectId(Long projectId) {
//         this.projectId = projectId;
//     }

//     public Long getConfigId() {
//         return configId;
//     }

//     public void setConfigId(Long configId) {
//         this.configId = configId;
//     }

//     @Override
//     public boolean equals(Object o) {
//         if (o == this)
//             return true;
//         if (!(o instanceof RelacionConfigProject))
//             return false;
//         RelacionConfigProject other = (RelacionConfigProject) o;
//         boolean valueEquals = (this.projectId == null && other.projectId == null) || (this.projectId != null && this.projectId.equals(other.projectId));
//         boolean storeEquals = (this.configId == null && other.configId == null) || (this.configId != null && this.configId.equals(other.configId));
//         return valueEquals && storeEquals;
//     }
// }
