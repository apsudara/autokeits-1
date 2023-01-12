// package dev.asudara.automation.bbdd;

// import javax.persistence.EmbeddedId;
// import javax.persistence.Entity;
// import javax.persistence.JoinColumn;
// import javax.persistence.ManyToOne;
// import javax.persistence.MapsId;

// @Entity
// class ConfigProjectJoin {

//     @EmbeddedId
//     RelacionConfigProject id;

//     @ManyToOne
//     @MapsId("configsId")
//     @JoinColumn(name = "configs_id")
//     ConfigurationModelo configs;

//     @ManyToOne
//     @MapsId("projectId")
//     @JoinColumn(name = "project_id")
//     ProjectModelo project;

//     // String nodoname;
//     public ConfigProjectJoin() {
//     }

//     public RelacionConfigProject getId() {
//         return id;
//     }

//     public void setId(RelacionConfigProject id) {
//         this.id = id;
//     }

//     public ConfigurationModelo getConfigs() {
//         return configs;
//     }

//     public void setConfigs(ConfigurationModelo configs) {
//         this.configs = configs;
//     }

//     public ProjectModelo getProject() {
//         return project;
//     }

//     public void setProject(ProjectModelo project) {
//         this.project = project;
//     }
    
    
// }