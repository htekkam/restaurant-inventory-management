
import api from "./api";

const ingredientService = {

    // GET all ingredients
    getAllIngredients: async () => {
        const response = await api.get("/ingredients");
        return response.data;
    },

    // GET ingredient by ID
    getIngredientById: async (id) => {
        const response = await api.get(`/ingredients/${id}`);
        return response.data;
    },

    // CREATE ingredient
    createIngredient: async (ingredient) => {
        const response = await api.post("/ingredients", ingredient);
        return response.data;
    },

    // UPDATE ingredient
    updateIngredient: async (id, ingredient) => {
        const response = await api.put(`/ingredients/${id}`, ingredient);
        return response.data;
    },

    // DEACTIVATE ingredient
    deactivateIngredient: async (id) => {
        await api.delete(`/ingredients/${id}`);
    }
};

export default ingredientService;
